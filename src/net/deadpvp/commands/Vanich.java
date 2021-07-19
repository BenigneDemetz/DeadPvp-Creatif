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

    public static ArrayList<Player> inVanish = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
        Player player = (Player) sender;
        if (inVanish.contains(player)) {

            for (Player p : onlinePlayers) {
                p.showPlayer(player);
            }


            player.sendMessage("§bVous n'êtes plus en vanish !");
            player.setPlayerListName(ChatUtils.getPrefix(player) +player.getName());
            inVanish.remove(player);
            return true;
        } else {
            if (!player.hasPermission("deadpvp.Vanish")) return true;
            for (Player p : onlinePlayers) {
                if (!p.hasPermission("deadpvp.Vanish")) {
                            p.hidePlayer(player);
                }
            }
            try {
                if (sqlUtilities.hasData("staffdetails", "staff", player.getName())) {
                    sqlUtilities.updateData("staffdetails", "vanished", true, "staff", player.getName());
                } else {
                    sqlUtilities.insertData("staffdetail", player.getName(), true, "vanished");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            player.sendMessage("§bVous êtes maintenant en vanish !");
            player.setPlayerListName("§7§l[VANISHED] "+player.getName());
            inVanish.add(player);
            }
            return true;
        }

    }