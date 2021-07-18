package net.deadpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcInvsee implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("chat.builder") || commandSender.isOp()) {

            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    commandSender.sendMessage("&cJoueur introuvable");
                    return true;
                }
                ((Player) commandSender).openInventory(target.getEnderChest());

            } else return false;
        }
        return false;
    }
}
