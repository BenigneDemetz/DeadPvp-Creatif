package net.deadpvp.events;

import com.yapzhenyie.GadgetsMenu.GadgetsMenu;
import net.deadpvp.Main;
import net.deadpvp.gui.GuiManager;
import net.deadpvp.gui.guis.MainGui;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftInventoryLectern;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.LecternInventory;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Objects;

public class InventoryListeners implements Listener {

    @EventHandler
    public void onInteractInventory(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;

        if (PlayerListeners.itemWithCommand(e.getCurrentItem(), p)) {
            e.setCancelled(true);
            e.setCurrentItem(new ItemStack(Material.AIR));
        }

        if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) e.getCurrentItem().getItemMeta();
            if (meta.getTitle().contains("§dCarnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                if (!e.isCancelled()) {
                    e.setCancelled(true);
                }
                new MainGui(Main.getPlayerGuiUtils(p)).openInv();
            }
        }


        InventoryHolder holder = e.getClickedInventory().getHolder();
        if (holder instanceof GuiManager) {

            if (!e.isCancelled()) {
                e.setCancelled(true);
            }
            if (e.getCurrentItem() == null) return;
            GuiManager gui = (GuiManager) holder;
            gui.EventHandler(e);
        }


    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e) {
        if (e.getView().getType().equals(InventoryType.LECTERN)) {
            e.setCancelled(PlayerListeners.itemWithCommand(e.getView().getItem(0), (Player) e.getPlayer()));
        }
        if (e.getView().getTitle().contains("Abuse Kit")) {
            e.setCancelled(true);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + e.getPlayer().getName());
        }
    }
}