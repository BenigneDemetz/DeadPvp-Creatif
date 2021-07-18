package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.commands.Vanich;
import net.deadpvp.gui.guis.MainGui;
import net.deadpvp.scoreboard.ScoreboardManager;
import net.deadpvp.utils.AdminInv;
import net.deadpvp.utils.ChatUtils;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerListeners implements Listener {

    static ArrayList<Player> punishedPlayers= new ArrayList<>();

    public static ItemStack book(){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("§4§lDEAD§9§lPVP");
        meta.setTitle("§dCarnet de commandes");
        book.setItemMeta(meta);
        return book;
    }

    /*
    * TODO: opti classe
    * */

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ScoreboardManager.setScoreBoard(p);
        p.getInventory().setItem(8, book());

        if (!p.hasPermission("deadpvp.vanich")) {
            for (Player playervanished : Vanich.inVanish) {
                p.hidePlayer(playervanished);
            }
        }

        Location spawn = new Location(Bukkit.getServer().getWorld("Creatif"), 0.5, 65.1, 80.5, 0, 0);
        p.teleport(spawn);
        p.setPlayerListName(ChatUtils.getPrefix(p) + p.getDisplayName());

        e.setJoinMessage("§a[§4+§a] " + ChatUtils.getPrefix(p) + e.getPlayer().getDisplayName());
        e.getPlayer().setGameMode(GameMode.CREATIVE);

        if (!p.hasPlayedBefore()) {
            Bukkit.broadcastMessage("§7Souhaitez la bienvenue à §6" + p.getName() + "§7 pour reçevoir une récompense !");
            ChatListeners.bienvenue = true;
            ChatListeners.hasSaidBienvenue.clear();
            p.sendMessage("§6Bienvenue sur le créatif de §4§lDEAD§1§lPVP §6!\n§6Pour créer un plot faites la commande /plot auto ou /plot claim sur un plot qui est vide.\nEn cas de problème contactez le staff sur §9discord: discord.gg/23kPxkbzDg");
            new BukkitRunnable() {
                @Override
                public void run() {
                    ChatListeners.bienvenue = false;
                }
            }.runTaskLater(Main.getInstance(), 300);
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Main.freeze.contains(e.getPlayer())) {
            e.setCancelled(true);
            e.getPlayer().sendTitle("§c§lVous êtes freeze !", "§6§lMerci de venir sur discord: discord.gg/23kPxkbzDg", 10, 0, 0);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() != null) {
            e.setCancelled(itemWithCommand(p.getItemOnCursor(),p,e.getClickedBlock()));
            e.setCancelled(itemWithCommand(e.getItem(),p,e.getClickedBlock()));
            if (e.getItem().getType().equals(Material.WRITTEN_BOOK)) {
                BookMeta meta = (BookMeta) e.getItem().getItemMeta();
                if (meta == null) return;
                if (!meta.hasTitle()) return;
                if (!meta.hasAuthor()) return;

                if (meta.getTitle().contains("§dCarnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                    e.setCancelled(true);
                    MainGui mainGui = new MainGui(Main.getPlayerGuiUtils(p));
                    mainGui.openInv();
                }
            }
            if (e.getClickedBlock() == null) return;
            if (Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.AIR)) return;
            if (Objects.requireNonNull(e.getClickedBlock()).getBlockData().getMaterial() == Material.BEACON && e.getAction() == e.getAction().RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
            }

            if (e.getItem() == null || e.getClickedBlock() == null) {
                return;
            }
            if ((e.getClickedBlock().getBlockData().getMaterial() == Material.END_PORTAL_FRAME && (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.ENDER_EYE
                    || e.getPlayer().getInventory().getItemInOffHand().getType() == Material.ENDER_EYE)) && e.getAction() == e.getAction().RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage("§d[§4-§d] " + ChatUtils.getPrefix(e.getPlayer()) + e.getPlayer().getName());
        ;
        Player player = e.getPlayer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }
        if (Vanich.inVanish.contains(e.getPlayer())) {
            AdminInv ai = AdminInv.getFromPlayer(player);
            ai.destroy();
            Main.getInstance().staffModePlayers.remove(player);
            ai.giveInv(player);
            player.sendMessage("§bVous n'êtes plus en vanish !");
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setPlayerListName(ChatUtils.getPrefix(player) + player.getName());
            Vanich.inVanish.remove(player);
        }
    }

    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("§4§lDEAD§9§lPVP");
        meta.setTitle("§dCarnet de commandes");
        book.setItemMeta(meta);

        p.getInventory().setItem(8, book);
    }

    public static boolean itemWithCommand (ItemStack itemToUse, Player p) {
        net.minecraft.server.v1_16_R1.ItemStack item = CraftItemStack.asNMSCopy(itemToUse);
        if (item.hasTag()) {
            NBTTagCompound nbt = item.getTag();
            if ((nbt.toString()).contains("run_command")) {
                System.out.println("§c" + p.getName() + " A TENTE DE METTRE UNE COMMANDE SUR UN ITEM ! ");
                for(Player staff : Bukkit.getOnlinePlayers()){
                    if(staff.hasPermission("chat.dev")){
                        staff.sendMessage("§c"+p.getName()+" §cvient d'essayer d'exécuter une commande avec un item !");
                    }
                }
                p.getInventory().clear();
                p.getInventory().setItem(8, book());
                p.closeInventory();
                punishRoom(p);
                return true;
            }
        }
        return false;
    }
    public static boolean itemWithCommand (ItemStack itemToUse, Player p, Block block) {
        net.minecraft.server.v1_16_R1.ItemStack item = CraftItemStack.asNMSCopy(itemToUse);
        if (item.hasTag()) {
            NBTTagCompound nbt = item.getTag();
            if ((nbt.toString()).contains("run_command")) {
                System.out.println("§c" + p.getName() + " A TENTE DE METTRE UNE COMMANDE SUR UN ITEM ! ");
                for(Player staff : Bukkit.getOnlinePlayers()){
                    if(staff.hasPermission("chat.dev")){
                        staff.sendMessage("§c"+p.getName()+" §cvient d'essayer d'exécuter une commande avec un item !");
                    }
                }
                p.getInventory().clear();
                p.getInventory().setItem(8, book());
                punishRoom(p);
                return true;
            }
        }
        return false;
    }

    public static void punishRoom (Player p) {
//        p.teleport(new Location(Bukkit.getWorld("Creatif"),0.5,20.1,0.5,0,0));
//        punishedPlayers.add(p);
//        p.setGameMode(GameMode.SURVIVAL);
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                p.damage(5);
//            }
//        }.runTaskTimer(Main.getInstance(),0,5);
    }

}
