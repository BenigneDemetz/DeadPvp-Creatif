package net.deadpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class heal implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player && commandSender.hasPermission("chat.apprenti")){
            Player p = (Player) commandSender;
            p.setHealth(p.getMaxHealth());
            p.sendMessage("§aVous venez de récuperer toute votre vie !");
            return true;
        }else{
            commandSender.sendMessage("§cVous n'avez pas les permissions pour faire cela !");
            return true;
        }

    }
}
