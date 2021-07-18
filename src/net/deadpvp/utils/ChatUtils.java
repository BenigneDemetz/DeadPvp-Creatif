package net.deadpvp.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatUtils {

    public static List<String> blockedCommands = new ArrayList<>();
    public static List<Player> hasSaidBienvenue = new ArrayList<>();
    public static Boolean bienvenue = false;
    public static Map<Player, Long> spam = new HashMap<Player, Long>();
    public static Map<Player, String> lastPlayerMessage = new HashMap<Player, String>();

    public static boolean containLink(String string) {
        return string.contains("http") && !string.contains("http://deadpvp.com/");
    }

    public static boolean canSayWelcome(Player p){
        return bienvenue && !hasSaidBienvenue.contains(p);
    }

    public static boolean isStaffChat(String msg){
        return msg.startsWith("!") || msg.startsWith("!!");
    }

    public static List<String> getBlockedCommands() {
        return blockedCommands;
    }

    public static String colorBienvenue(String msg){
        msg = msg.replaceAll("(?i)bienvenue","§6§lBienvenue§f");
        return msg;
    }

    public static boolean hasAlreadySentMessage(Player p, String string){
        return lastPlayerMessage.containsKey(p) && string.equalsIgnoreCase(lastPlayerMessage.get(p));
    }

    public static boolean isInCooldown(Player p){
        return spam.containsKey(p) && spam.get(p) >= System.currentTimeMillis();
    }

    public static void setCooldown(Player p, long cooldown){
        cooldown = System.currentTimeMillis() + cooldown*1000;
        spam.put(p, cooldown);
    }

    public static boolean isLockedCommand(String string, List<String> blockedCommands) {
        for (String str : blockedCommands){
            if(string.startsWith(str)) return true;
        }
        return false;
    }

    public static String staffChat(String string, Player sender){

        if(string.startsWith("!!")){
            string = "§c[AdminChat] " + ChatUtils.getPrefixColor(sender) + sender.getName() + "§6: " + string;
            string = string.replaceFirst("!!", "");
        } else if(string.startsWith("!")){
            string = "§d[StaffChat] " + ChatUtils.getPrefixColor(sender) + sender.getName() + "§6: " + string;
            string = string.replaceFirst("!", "");
        }

        return string;
    }

    public static int getUpperCaseNumber(String string){
        int maj = 0;
        for (int k = 0; k < string.length(); k++) {
            if (Character.isUpperCase(string.charAt(k))) {
                maj++;
            }
        }
        return maj;
    }

    public static void addKarmaAndList(Player p) {
        try {
            int karmatogive = new Random().nextInt(4) + 1;
            if (sqlUtilities.hasData("moneyserv", "player", p.getName())) {
                int data = (int) sqlUtilities.getData("moneyserv","player",p.getName(), "karma","Int" );
                sqlUtilities.updateData("moneyserv", "karma", data + karmatogive, p.getName());
            }
            else {
                sqlUtilities.insertData("moneyserv", p.getName(), 0, karmatogive, "player, mystiques, karma");
            }
            p.sendMessage("§d+"+karmatogive+" Karma");
            hasSaidBienvenue.add(p);
        }
        catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void registerBlockedCommands() {
        blockedCommands.add("/minecraft:list");
        blockedCommands.add("/list");
        blockedCommands.add("/pl");
        blockedCommands.add("/bukkit");
        blockedCommands.add("/?");
        blockedCommands.add("/help");
        blockedCommands.add("/perm");
        blockedCommands.add("/zperm");
    }

    public static String getPrefix(Player p) {
        if (p.getName().equals("uhu376")) return "§9";
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
        if (p.getName().equals("uhu376")) return "§9";
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
