package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.utils.BookUtils;
import net.deadpvp.utils.GuiUtils;
import net.deadpvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class EventListener implements Listener {

    public static ArrayList<Player> hasAccepted = new ArrayList<>();
    private static final ItemStack book = BookUtils.createBook();

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().setGameMode(GameMode.CREATIVE);
            }
        }.runTaskLater(Main.getInstance(), 20L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage("§2[§4+§a] " + e.getPlayer().getDisplayName());
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().setGameMode(GameMode.CREATIVE);
            }
        }.runTaskLater(Main.getInstance(), 20L);

        if (!p.hasPlayedBefore()) {
            BookUtils.openBook(book, p);
        }
        else hasAccepted.add(p);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("§4§lDEAD§9§lPVP");
        meta.setTitle("Carnet de commandes");
        book.setItemMeta(meta);

        p.getInventory().setItem(8, book);

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        //aucun event existe pour savoir quand le mec ferme un book donc j'ai un peu triché, l'event se trigger meme si il ne bouge que sa tete.
        if (!hasAccepted.contains(e.getPlayer())) {
            BookUtils.openBook(book, e.getPlayer());
            e.getPlayer().sendMessage("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!hasAccepted.contains(e.getPlayer())) {
            BookUtils.openBook(book, e.getPlayer());
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (!hasAccepted.contains(e.getPlayer()) && !e.getMessage().equals("/dpaccept")) {
            BookUtils.openBook(book, e.getPlayer());
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) book.getItemMeta();
                meta.setAuthor("§4§lDEAD§9§lPVP");
                meta.setTitle("Carnet de commandes");
                book.setItemMeta(meta);

                p.getInventory().setItem(8, book);
            }
        }.runTaskLater(Main.getInstance(), 5L);

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage("§c[§4-§d] " + e.getPlayer().getDisplayName());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() != null) {
            if (e.getItem().getType().equals(Material.WRITTEN_BOOK)) {
                BookMeta meta = (BookMeta) e.getItem().getItemMeta();
                if (meta.getTitle().contains("Carnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                    e.setCancelled(true);
                    p.openInventory(GuiUtils.mainMenu(p));
                }
            }
        }
    }

    @EventHandler
    public void onInteractInventory (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) return;

        if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) e.getCurrentItem().getItemMeta();
            if (meta.getTitle().contains("Carnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                e.setCancelled(true);
                p.openInventory(GuiUtils.mainMenu(p));
            }
        } //clic livre in inventory

        if (e.getView().getTitle().equals("§bCommandes")) {
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()) {
                case IRON_SWORD:
                    resetItemsGamemode(p);
                    p.setGameMode(GameMode.ADVENTURE);
                    ItemBuilder aventure = new ItemBuilder(Material.IRON_SWORD).setName("§dMode Aventure");
                    ItemStack aventureItemStack = aventure.toItemStack();
                    aventureItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
                    ItemMeta itemMetaAventure = aventureItemStack.getItemMeta();
                    itemMetaAventure.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    itemMetaAventure.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    aventureItemStack.setItemMeta(itemMetaAventure);
                    p.getOpenInventory().setItem(3*9+5, aventureItemStack);
                    break;
                case GLASS:
                    resetItemsGamemode(p);
                    p.setGameMode(GameMode.SPECTATOR);
                    ItemBuilder spec = new ItemBuilder(Material.GLASS).setName("§dMode Spectateur");
                    ItemStack specItemStack = spec.toItemStack();
                    specItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
                    ItemMeta itemMetaSpec = specItemStack.getItemMeta();
                    itemMetaSpec.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    specItemStack.setItemMeta(itemMetaSpec);
                    p.getOpenInventory().setItem(3*9+6, specItemStack);
                    break;
                case STONE_PICKAXE:
                    resetItemsGamemode(p);
                    p.setGameMode(GameMode.SURVIVAL);
                    ItemBuilder survie = new ItemBuilder(Material.STONE_PICKAXE).setName("§dMode Survie");
                    ItemStack survieItemStack = survie.toItemStack();
                    survieItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                    ItemMeta itemMetaSurvie = survieItemStack.getItemMeta();
                    itemMetaSurvie.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    itemMetaSurvie.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    survieItemStack.setItemMeta(itemMetaSurvie);
                    p.getOpenInventory().setItem(3*9+7, survieItemStack);
                    break;
                case CRAFTING_TABLE:
                    resetItemsGamemode(p);
                    p.setGameMode(GameMode.CREATIVE);
                    ItemBuilder crea = new ItemBuilder(Material.CRAFTING_TABLE).setName("§dMode Créatif");
                    ItemStack itemStackCrea = crea.toItemStack();
                    itemStackCrea.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
                    ItemMeta imcrea = crea.toItemStack().getItemMeta();
                    imcrea.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                    itemStackCrea.setItemMeta(imcrea);
                    p.getOpenInventory().setItem(3*9+8,itemStackCrea);
                    break;
                case GRASS:
                    p.performCommand("");
                    break;
                case OAK_DOOR:
                    p.performCommand("p home");
                    break;

            }

        }
    }

    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("§4§lDEAD§9§lPVP");
        meta.setTitle("Carnet de commandes");
        book.setItemMeta(meta);

        p.getInventory().setItem(8, book);
    }

    @EventHandler
    public void onDropItem (PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        e.getItemDrop();
        if (e.getItemDrop().getItemStack().getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) e.getItemDrop().getItemStack().getItemMeta();
            if (meta.getTitle().contains("Carnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onItemSpawn (ItemSpawnEvent e) {
        if (e.getEntity().getItemStack().getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) e.getEntity().getItemStack().getItemMeta();
            if (meta.getTitle().contains("Carnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                e.setCancelled(true);
            }
        }
    }

    private void resetItemsGamemode (Player p ) {

        ItemBuilder aventure = new ItemBuilder(Material.IRON_SWORD).setName("§dMode Aventure");

        ItemBuilder spec = new ItemBuilder(Material.GLASS).setName("§dMode Spectateur");

        ItemBuilder survie = new ItemBuilder(Material.STONE_PICKAXE).setName("§dMode Survie");

        ItemBuilder crea = new ItemBuilder(Material.CRAFTING_TABLE).setName("§dMode Créatif");

        p.getOpenInventory().setItem(3*9+5, aventure.toItemStack());
        p.getOpenInventory().setItem(3*9+6, spec.toItemStack());
        p.getOpenInventory().setItem(3*9+7, survie.toItemStack());
        p.getOpenInventory().setItem(3*9+8, crea.toItemStack());
    }

}