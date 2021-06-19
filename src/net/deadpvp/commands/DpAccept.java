package net.deadpvp.commands;

import net.deadpvp.events.EventListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DpAccept implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            EventListener.hasAccepted.add (p);
        }
        return false;
    }
}
