package net.deadpvp.commands;

import net.deadpvp.Main;
import net.deadpvp.events.EventListener;
import net.deadpvp.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Nick implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            commandSender.sendMessage("§cErreur: commande temporairement désactivé !");
            return true;
        }
        if (!(commandSender.hasPermission("deadpvp.constructeur"))) return true;

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
            p.setPlayerListName(ChatUtils.getPrefix(p) + name);
            p.setDisplayName(ChatUtils.getPrefixColor(p) + name);
            p.setCustomName(p.getName());

            p.sendMessage("§aTon pseudo est maintenant §d" + name);

            Main.getInstance().nickname.put(p.getName(), name);

            Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(Main.getInstance(), p));
            Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(Main.getInstance(), p));
    }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(players -> list.add(players.getName()));
        Bukkit.getOnlinePlayers().forEach(players -> list.add(players.getDisplayName()));

        return list;
    }
}
