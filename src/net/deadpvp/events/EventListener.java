package net.deadpvp.events;


import com.yapzhenyie.GadgetsMenu.GadgetsMenu;
import net.deadpvp.Main;
import net.deadpvp.commands.Vanich;
import net.deadpvp.utils.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.sql.SQLException;
import java.util.*;

public class EventListener implements Listener {
    public static Map<Player,Player> lastdamage = new HashMap<Player,Player>();

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        e.getPlayer().setGameMode(GameMode.CREATIVE);
    }



    @EventHandler
    public void Signit(SignChangeEvent e){
        if(e.getPlayer().hasPermission("chat.apprenti") || e.getPlayer().hasPermission("chat.builder")){
            String line1 = e.getLine(0);
            String line2 = e.getLine(1);
            String line3 = e.getLine(2);
            String line4 = e.getLine(3);
            e.setLine(0, line1.replace("&","§"));
            e.setLine(1, line2.replace("&","§"));
            e.setLine(2, line3.replace("&","§"));
            e.setLine(3, line4.replace("&","§"));
        }
        for(int i=0;i < 4;i++){
            if(e.getLine(i).toLowerCase().contains("http") || e.getLine(i).toLowerCase().contains("www") || e.getLine(i).toLowerCase().contains("://")){
                if(!e.getLine(i).toLowerCase().contains("http://deadpvp.com/")){
                    e.setLine(0,"§4LIEN INTERDIT ");
                    e.setLine(1,"§4youtu.be/");
                    e.setLine(2,"§4dQw4w9WgXcQ");
                    e.setLine(3,"§d§l§kDDDDDDDDD");
                }
            }
        }

    }
    @EventHandler
    public void OnCreaturespawn(CreatureSpawnEvent e){
        if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN
        ||e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_WITHER || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM){
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void createPortal(PortalCreateEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDropItem (PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        e.getItemDrop();
        if (e.getItemDrop().getItemStack().getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) e.getItemDrop().getItemStack().getItemMeta();
            if (meta.getTitle().contains("§dCarnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemSpawn (ItemSpawnEvent e) {
        if (e.getEntity().getItemStack().getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) e.getEntity().getItemStack().getItemMeta();
            if (meta.getTitle().contains("§dCarnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onWeatherChange (WeatherChangeEvent e) {
        e.setCancelled(true);
    }
}