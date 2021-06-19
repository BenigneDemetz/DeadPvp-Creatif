package net.deadpvp;

import net.deadpvp.commands.DpAccept;
import net.deadpvp.commands.hub;
import net.deadpvp.events.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements PluginMessageListener {
    private static Main instance;

    
    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        PluginManager pm = Bukkit.getServer ().getPluginManager ();
        pm.registerEvents (new EventListener (), this);
        getCommand ("hub").setExecutor (new hub (this));
        getCommand ("dpaccept").setExecutor (new DpAccept ());
    
        new BukkitRunnable () {
            @Override
            public void run() {
                for(Player pl : Bukkit.getOnlinePlayers ()){
                    pl.setGameMode (GameMode.CREATIVE);
                }
            }
        }.runTaskLater (Main.getInstance (), 20L);
        
        super.onEnable ();
    }
    
    @Override
    public void onDisable() {
        
        
        super.onDisable ();
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    @Override
    public void onPluginMessageReceived (String s, Player player, byte[] bytes) {
    
    }
}
