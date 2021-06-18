package net.deadpvp.events;

import net.deadpvp.Main;
import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EventListener implements Listener {
    //Main main;
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
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage ("§2[§4+§a] " + e.getPlayer ().getDisplayName ());
        new BukkitRunnable () {
            @Override
            public void run() {
                e.getPlayer ().setGameMode (GameMode.CREATIVE);
            }
        }.runTaskLater (Main.getInstance (), 20L);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage ("§2[§4+§a] " + e.getPlayer ().getDisplayName ());
    }
}
