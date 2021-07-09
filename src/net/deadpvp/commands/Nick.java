package net.deadpvp.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import net.deadpvp.Main;
import net.deadpvp.events.EventListener;
import net.deadpvp.utils.NameChanger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;

public class Nick implements CommandExecutor, TabCompleter {


    private String nametoset;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("deadpvp.constructeur")) {
            if (!(commandSender instanceof Player)) return false;

            Player p = (Player) commandSender;

            if (strings.length >= 1) {
                String name = "";
                if (strings[0].equals("reset")) {
                    name += p.getName();
                    Main.getInstance().nickname.put(p.getName(), name);

                } else {
                    for (String args : strings) {
                        name += args;
                    }
                }


                if (p.isOp()) {
                    name = name.replace("&k", "§k");
                }
                if (name.length() >= 13) {
                    p.sendMessage("§cTu ne peux pas dépasser 13 caractères");
                    return true;
                }
                p.setPlayerListName(EventListener.getPrefix(p) + name);
                p.setDisplayName(getColor(p) + name);
                p.setCustomName(p.getName());

                p.sendMessage("§aTon pseudo est maintenant §d" + name);

                Main.getInstance().nickname.put(p.getName(), name);

                Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(Main.getInstance(), p));
                Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(Main.getInstance(), p));


            } else return false;
        }

        return true;
    }

    public static ChatColor getColor (Player p) {
        if (p.getName().equals("Red_Spash")) return ChatColor.RED;
        if (p.getName().equals("Arnaud013")) return ChatColor.RED;
        if (p.hasPermission("chat.admin")) return ChatColor.DARK_RED;
        if (p.hasPermission("chat.dev")) return ChatColor.RED;
        if (p.hasPermission("chat.modo")) return ChatColor.GOLD;
        if (p.hasPermission("chat.builder")) return ChatColor.BLUE;
        if (p.hasPermission("chat.vip")) return ChatColor.AQUA;
        if (p.hasPermission("deadpvp.architecte")) return ChatColor.AQUA;
        if (p.hasPermission("deadpvp.constructeur")) return ChatColor.DARK_AQUA;
        if (p.hasPermission("deadpvp.apprenti")) return ChatColor.GREEN;


        else return ChatColor.GRAY;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(players -> list.add(players.getName()));
        Bukkit.getOnlinePlayers().forEach(players -> list.add(players.getDisplayName()));

        return list;
    }
}
