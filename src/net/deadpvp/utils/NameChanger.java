package net.deadpvp.utils;/*
* The MIT License
* Copyright (c) 2015 Techcable
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
import java.util.*;

import net.deadpvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class allows you to change a player's nametag
 * <p>
 * It requires ProtocolLib and only works on 1.8-1.8.3
 * If you need a 1.7 version, use TagAPI
 * </p>
 * 
 * @author Techcable
 */
public class NameChanger {
    public static final Map<Player, String> fakeNames = new WeakHashMap<Player, String>();
    private static Plugin plugin;
    public NameChanger(Plugin plugin) {
        this.plugin = plugin;
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getPlayerInfoAction().read(0) != PlayerInfoAction.ADD_PLAYER) return;
                List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<PlayerInfoData>();
                List<PlayerInfoData> playerInfoDataList = event.getPacket().getPlayerInfoDataLists().read(0);
                for (PlayerInfoData playerInfoData : playerInfoDataList) {
                    if (playerInfoData == null || playerInfoData.getProfile() == null || Bukkit.getPlayer(playerInfoData.getProfile().getUUID()) == null) { //Unknown Player
                        newPlayerInfoDataList.add(playerInfoData);
                        continue;
                    }
                    WrappedGameProfile profile = playerInfoData.getProfile();
                    profile = profile.withName(getNameToSend(profile.getUUID()));
                    PlayerInfoData newPlayerInfoData = new PlayerInfoData(profile, playerInfoData.getPing(), playerInfoData.getGameMode(), playerInfoData.getDisplayName());
                    newPlayerInfoDataList.add(newPlayerInfoData);
                }
                event.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfoDataList);
            }
        });
    }

    public static String getNameToSend(UUID id) {
        Player player = Bukkit.getPlayer(id);
        if (!fakeNames.containsKey(player)) return player.getName();
        return fakeNames.get(player);
    }
    
    /**
     * Change the player's name to the provided string
     * <br>
     * The player may disappear for approximately 2 ticks after you change it
     * </br>
     * @param player player whos name to change
     * @param fakeName the player's new name
     */
    public static void changeName(final Player player, String fakeName) {
        fakeNames.put(player, fakeName);
        refresh(player);
    }

    
    public static void refresh(final Player player) {
        for (final Player forWhom : player.getWorld().getPlayers()) {
            if (player.equals(forWhom) || !player.getWorld().equals(forWhom.getWorld()) || !forWhom.canSee(player)) {
                forWhom.hidePlayer(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        forWhom.showPlayer(player);
                    }
                }.runTaskLater(Main.getInstance(), 2);
            }
        }
    }
}