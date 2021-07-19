package net.deadpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class feed implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player && commandSender.hasPermission("chat.apprenti")){
            Player p = (Player) commandSender;
            p.setFoodLevel(30);
            p.sendMessage("§aVous venez de récuperer toute votre nourriture !");
            return true;
        }
        return false;
    }
}
