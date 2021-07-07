package net.deadpvp;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.deadpvp.commands.DpAccept;
import net.deadpvp.commands.Speed;
import net.deadpvp.commands.TestCommand;
import net.deadpvp.commands.hub;
import net.deadpvp.events.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;
import java.sql.*;

public class Main extends JavaPlugin implements PluginMessageListener {
    private static Main instance;

    private Connection connection;
    public String host, database, username, password;
    public int port;

    
    @Override
    public void onEnable() {
        mysqlSetup();
        instance = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "deadpvp:return", this);
        PluginManager pm = Bukkit.getServer ().getPluginManager ();
        pm.registerEvents(new EventListener(), this);
        getCommand ("hub").setExecutor (new hub (this));
        getCommand ("dpaccept").setExecutor (new DpAccept ());
//        getCommand ("test").setExecutor (new TestCommand ());
        getCommand ("speed").setExecutor (new Speed());
        new BukkitRunnable () {
            @Override
            public void run() {
                for(Player pl : Bukkit.getOnlinePlayers ()){
                    pl.setGameMode (GameMode.CREATIVE);
                    EventListener.hasAccepted.add(pl);
                }
            }
        }.runTaskLater (Main.getInstance (), 20L);

        for(Player pl : Bukkit.getOnlinePlayers ()){
            pl.setGameMode (GameMode.CREATIVE);
            EventListener.hasAccepted.add(pl);
        }
        super.onEnable ();
    }
    
    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this, "deadpvp:return", this);
        
        
        super.onDisable ();
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    @Override
    public void onPluginMessageReceived (String s, Player player, byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subchannel = in.readUTF();
        if (subchannel.equals("command")) {
            String cmd = in.readUTF();
            System.out.println("[Boutiuqe]");
            System.out.println(cmd);
            player.setPlayerListName(EventListener.getPrefix(player)+player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public void mysqlSetup(){
        host = "localhost";
        port = 3306;
        database = "minecraftrebased";
        username = "root";
        password = "";

        try {

            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password));

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
            }
        }
        catch(SQLException e){
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "MYSQL NOT CONNECTED #2");
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "MYSQL NOT CONNECTED #3");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }
}
