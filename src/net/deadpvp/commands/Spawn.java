package net.deadpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Location spawn = new Location(Bukkit.getServer().getWorld("Creatif"), 0.5,65.1,80.5, 0, 0);
        ((Player) commandSender).teleport(spawn);

        return false;
    }
}
