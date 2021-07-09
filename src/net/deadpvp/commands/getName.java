package net.deadpvp.commands;

import net.deadpvp.Main;
import net.deadpvp.events.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getName implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender.hasPermission("deadpvp.constructeur")) {
            if (!(commandSender instanceof Player)) return false;

            Player p = (Player) commandSender;

            if (strings.length >= 1) {
                String name = "";
                try {
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        if (target.getDisplayName().contains(strings[0])) p.sendMessage("§aLe pseudo du joueur ciblé est §d" + target.getName());
                    }
                }
                catch (Exception e) {
                    p.sendMessage("§cLe joueur n'est pas connecté");
                }


            } else return false;
        }

        return true;
    }
}
