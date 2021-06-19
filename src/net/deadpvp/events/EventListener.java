package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.utils.BookUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class EventListener implements Listener {
    
    public static ArrayList<Player> hasAccepted = new ArrayList<> ();
    private static final ItemStack book = BookUtils.createBook ();
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        new BukkitRunnable () {
            @Override
            public void run() {
                e.getPlayer ().setGameMode (GameMode.CREATIVE);
            }
        }.runTaskLater (Main.getInstance (), 20L);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer ();
        e.setJoinMessage ("§2[§4+§a] " + e.getPlayer ().getDisplayName ());
        new BukkitRunnable () {
            @Override
            public void run() {
                e.getPlayer ().setGameMode (GameMode.CREATIVE);
            }
        }.runTaskLater (Main.getInstance (), 20L);

        if(!p.hasPlayedBefore ()) {
            BookUtils.openBook (book, p);
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        //aucun event existe pour savoir quand le mec ferme un book donc j'ai un peu triché, l'event se trigger meme si il ne bouge que sa tete.
        if(!e.getPlayer ().hasPlayedBefore () && !hasAccepted.contains (e.getPlayer ())){
            BookUtils.openBook (book, e.getPlayer ());
            e.getPlayer ().sendMessage ("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
        }
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(!hasAccepted.contains (e.getPlayer ())){
            BookUtils.openBook (book, e.getPlayer ());
            e.setCancelled (true);
            e.getPlayer ().sendMessage ("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
        }
    }
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        if(!hasAccepted.contains (e.getPlayer ()) && !e.getMessage ().equals ("/dpaccept")){
            BookUtils.openBook (book, e.getPlayer ());
            e.setCancelled (true);
            e.getPlayer ().sendMessage ("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage("§c[§4-§d] " + e.getPlayer ().getDisplayName ());;
    }
    
}
