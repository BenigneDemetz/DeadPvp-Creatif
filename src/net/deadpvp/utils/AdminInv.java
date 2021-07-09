package net.deadpvp.utils;

import net.deadpvp.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminInv {

    Main main;

    private GameMode gmad;

    private Player player;

    private ItemStack[] items = new ItemStack[40];

    public AdminInv(Player player) {
        this.player = player;
    }

    public void init() {
        Main.getInstance().adminPlayerHashmap.put(player, this);
    }

    public void destroy() {
        Main.getInstance().adminPlayerHashmap.remove(player);
    }

    public static AdminInv getFromPlayer(Player player) {
        return Main.getInstance().adminPlayerHashmap.get(player);
    }

    public void saveInv(Player player) {
    gmad = player.getGameMode();
    for(int slot = 0; slot < 36; slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if(item != null) {
                items[slot] = item;
            }
        }
        items[36] = player.getInventory().getHelmet();
        items[37] = player.getInventory().getChestplate();
        items[38] = player.getInventory().getLeggings();
        items[39] = player.getInventory().getBoots();

        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public void giveInv(Player player) {
        player.setGameMode(gmad);
        player.getInventory().clear();
        for(int slot = 0; slot < 36; slot++) {
            ItemStack item = items[slot];
            if(item != null) {
                player.getInventory().setItem(slot, item);
            }
        }
        player.getInventory().setHelmet(items[36]);
        player.getInventory().setChestplate(items[37]);
        player.getInventory().setLeggings(items[38]);
        player.getInventory().setBoots(items[39]);
    }
}
