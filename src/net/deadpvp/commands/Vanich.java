package net.deadpvp.commands;

import net.deadpvp.Main;
import net.deadpvp.events.EventListener;
import net.deadpvp.utils.AdminInv;
import net.deadpvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Vanich implements CommandExecutor {
    public static ArrayList<Player> inVanish = new ArrayList<Player>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
            Player player = ((Player) sender).getPlayer();
            if (inVanish.contains(player)) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    player.setAllowFlight(true);
                    player.setFlying(false);

                }
                for (Player p : onlinePlayers) {
                    p.showPlayer(player);
                }
                AdminInv ai = AdminInv.getFromPlayer(player);
                ai.destroy();
                Main.getInstance().staffModePlayers.remove(player);
                ai.giveInv(player);
                player.sendMessage("§bVous n'êtes plus en vanish !");
                player.setPlayerListName(EventListener.getPrefix(player) +player.getName());
                inVanish.remove(player);
                return true;

            }

            if (!inVanish.contains(player)) {
                if (player.hasPermission("deadpvp.Vanish")) {
                    for (Player p : onlinePlayers) {
                        if (p.hasPermission("deadpvp.Vanish")) {
                            p.showPlayer(player);
                        } else {
                            p.hidePlayer(player);
                        }
                    }
                    player.sendMessage("§bVous êtes maintenant en vanish !");
                    player.setPlayerListName("§7§l[VANISHED] "+player.getName());
                    inVanish.add(player);
                    AdminInv ai = new AdminInv(player);
                    ai.init();
                    Main.getInstance().staffModePlayers.add(player);
                    ai.saveInv(player);

                } else {
                    player.sendMessage("§cVous n'avez pas les permissions de faire cela !");
                }
            } else {
                for (Player p : onlinePlayers) {
                    p.showPlayer(player);
                }
            }
            return true;
        }
        return false;
    }


}