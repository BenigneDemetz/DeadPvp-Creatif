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
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender.hasPermission("deadpvp.constructeur")) {
            if (!(commandSender instanceof Player)) return true;

            Player p = (Player) commandSender;

            if (args.length == 1) {
                String name = "";
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    p.sendMessage("&cJoueur introuvable");
                    return true;
                }
                p.sendMessage("Le nom du joueur est : " + target.getDisplayName());

            } else return false;
        }

        return true;
    }
}
