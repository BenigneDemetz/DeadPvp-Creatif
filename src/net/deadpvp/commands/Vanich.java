package net.deadpvp.commands;

import net.deadpvp.Main;
import net.deadpvp.events.EventListener;
import net.deadpvp.utils.AdminInv;
import net.deadpvp.utils.ChatUtils;
import net.deadpvp.utils.sqlUtilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class Vanich implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {

            if (!(sender instanceof Player)) return true;


            Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
            Player player = (Player) sender;

            if (sqlUtilities.hasData("staffdetails", "staff", player.getName())) {
                Object isvanish = null;
                isvanish = sqlUtilities.getData("staffdetails", "staff", player.getName(), "vanished", "Boolean");
                System.out.println(isvanish);

                if (isvanish.equals(true)) {

                    sqlUtilities.updateData("staffdetails","vanished", false, "staff", player.getName());

                    for (Player p : onlinePlayers) {
                        p.showPlayer(player);
                    }

                    player.setAllowFlight(true);
                    player.setFlying(true);

                    player.sendMessage("§bVous n'êtes plus en vanish !");
                    player.setPlayerListName(ChatUtils.getPrefix(player) + player.getName());
                    return true;
                } else {
                    if (!player.hasPermission("deadpvp.Vanish")) return true;
                    sqlUtilities.updateData("staffdetails","vanished", true,"staff", player.getName());
                    for (Player p : onlinePlayers) {
                        if (!p.hasPermission("deadpvp.Vanish")) {
                            p.hidePlayer(player);
                        }
                    }
                    player.sendMessage("§bVous êtes maintenant en vanish !");
                    player.setPlayerListName("§7§l[VANISHED] " + player.getName());
                }
            } else {
                sqlUtilities.insertData("staffdetails", player.getName(), false, "staff, vanished");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sender.sendMessage("§cUne erreur a eu lieu");
        return true;
    }

}