package net.deadpvp.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {

    public static String getPrefix(Player p) {
        if (p.getName().equals("Red_Spash")) return ChatColor.RED+"[Développeur] ";
        if (p.getName().equals("Arnaud013")) return ChatColor.RED+"[Développeur] ";
        if (p.hasPermission("chat.admin")) return ChatColor.DARK_RED+"[Administrateur] ";
        if (p.hasPermission("chat.dev")) return ChatColor.RED+"[Développeur] ";
        if (p.hasPermission("chat.modo")) return ChatColor.GOLD+"[Modérateur] ";
        if (p.hasPermission("chat.builder")) return ChatColor.BLUE+"[Builder] ";
        if (p.hasPermission("deadpvp.architecte")) return "§b[Architecte] §b";
        if (p.hasPermission("deadpvp.constructeur")) return "§3[Constructeur] §3";
        if (p.hasPermission("deadpvp.apprenti")) return "§a[Apprenti] §a";


        else return "§7";
    }

    public static String getPrefixColor(Player p) {
        if (p.getName().equals("Red_Spash")) return "§c";
        if (p.getName().equals("Arnaud013")) return "§c";
        if (p.hasPermission("chat.admin")) return "§4";
        if (p.hasPermission("chat.dev")) return "§c";
        if (p.hasPermission("chat.modo")) return "§6";
        if (p.hasPermission("chat.builder")) return "§9";
        if (p.hasPermission("chat.vip")) return "§b";
        if (p.hasPermission("deadpvp.architecte")) return "§b";
        if (p.hasPermission("deadpvp.constructeur")) return "§3";
        if (p.hasPermission("deadpvp.apprenti")) return "§a";


        else return "§7";
    }
}
