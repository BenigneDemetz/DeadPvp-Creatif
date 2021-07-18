package net.deadpvp;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.deadpvp.commands.*;
import net.deadpvp.events.ChatListeners;
import net.deadpvp.events.EventListener;
import net.deadpvp.events.InventoryListeners;
import net.deadpvp.events.PlayerListeners;
import net.deadpvp.gui.PlayerGuiUtils;
import net.deadpvp.scoreboard.ScoreboardManager;
import net.deadpvp.utils.AdminInv;
import net.deadpvp.utils.ChatUtils;
import net.deadpvp.utils.sqlUtilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin implements PluginMessageListener {

    private static Main instance;
    public int playerCount;
    private Connection connection;

    public static ArrayList<Player> freeze = new ArrayList<> ();
    public ArrayList<Player> staffModePlayers = new ArrayList<Player>();
    public HashMap<Player, AdminInv> adminPlayerHashmap = new HashMap<>();
    public HashMap<String, String> nickname = new HashMap<>();
    private static final HashMap<Player, PlayerGuiUtils> playerGuiUtilsMap = new HashMap();

    
    @Override
    public void onEnable() {
        instance = this;
        sqlUtilities.mysqlSetup();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "deadpvp:return", this);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new ScoreboardManager(), 10L, 10L);
        registerCommands();
        registerEvents();
        registerProtocolLib();
        ChatUtils.registerBlockedCommands();

        super.onEnable ();
    }

    public static Main getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this, "deadpvp:return", this);

        super.onDisable ();
    }

    public void registerEvents(){
        PluginManager pm = Bukkit.getServer ().getPluginManager ();
        pm.registerEvents(new EventListener(), this);
        pm.registerEvents(new InventoryListeners(), this);
        pm.registerEvents(new ChatListeners(), this);
        pm.registerEvents(new PlayerListeners(), this);
    }

    public void registerCommands(){
        getCommand ("hub").setExecutor (new hub (this));
        getCommand ("speed").setExecutor (new Speed());
        getCommand ("spawn").setExecutor (new Spawn());
        getCommand ("livrebeta").setExecutor (new LivreBeta());
        getCommand("Vanish").setExecutor(new Vanich());
        getCommand("tpa").setExecutor(new Tpa());
        getCommand("tpyes").setExecutor(new Tpyes());
        getCommand("tpno").setExecutor(new Tpno());
        getCommand ("nick").setExecutor (new Nick());
        getCommand ("freeze").setExecutor (new freeze());
        getCommand ("getname").setExecutor (new getName());
        getCommand ("tpahere").setExecutor (new Tpahere());
        getCommand ("invsee").setExecutor (new Invsee());
        getCommand ("ecsee").setExecutor (new EcInvsee());
    }

    public static PlayerGuiUtils getPlayerGuiUtils(Player p){
        PlayerGuiUtils playerGuiUtils;
        if(!playerGuiUtilsMap.containsKey(p)){
            playerGuiUtils = new PlayerGuiUtils(p);
            playerGuiUtilsMap.put(p, playerGuiUtils);
            return playerGuiUtils;
        } else {
            return playerGuiUtilsMap.get(p);
        }
    }

    @Override
    public void onPluginMessageReceived (String s, Player player, byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subchannel = in.readUTF();
        if (subchannel.equals("command")) {
            String cmd = in.readUTF();
            System.out.println("[Boutique]");
            System.out.println(cmd);
            player.setPlayerListName(ChatUtils.getPrefix(player)+player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
        if (subchannel.equals ("PlayerCount")) {
            String server = in.readUTF();
            if (server.equalsIgnoreCase("all")) {
                playerCount = in.readInt();
            }
        }
    }


    public void registerProtocolLib(){
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), PacketType.Play.Server.PLAYER_INFO) {

            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.ADD_PLAYER) return;
                List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<PlayerInfoData>();
                List<PlayerInfoData> playerInfoDataList = event.getPacket().getPlayerInfoDataLists().read(0);
                for (PlayerInfoData playerInfoData : playerInfoDataList) {
                    if (playerInfoData == null || playerInfoData.getProfile() == null || Bukkit.getPlayer(playerInfoData.getProfile().getUUID()) == null) { //Unknown Player
                        newPlayerInfoDataList.add(playerInfoData);
                        continue;
                    }
                    WrappedGameProfile profile = playerInfoData.getProfile();
                    profile = profile.withName(profile.getName());

                    if (nickname.containsKey(profile.getName())) profile =
                            profile.withName(ChatUtils.getPrefixColor(Bukkit.getPlayer(profile.getName())) + nickname.get(profile.getName()));
                    PlayerInfoData newPlayerInfoData = new PlayerInfoData(profile, playerInfoData.getPing(), playerInfoData.getGameMode(), playerInfoData.getDisplayName());

                    newPlayerInfoDataList.add(newPlayerInfoData);
//                    if (nickname.containsKey(profile.getName()))
//                        Bukkit.getPlayer(profile.getName()).setPlayerListName(EventListener.getPrefixColor(Bukkit.getPlayer(profile.getName()))+ nickname.get(profile.getName()));

                }
                event.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfoDataList);
            }
        });
    }
}
