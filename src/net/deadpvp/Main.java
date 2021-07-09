package net.deadpvp;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.deadpvp.commands.*;
import net.deadpvp.events.EventListener;
import net.deadpvp.utils.AdminInv;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin implements PluginMessageListener {
    private static Main instance;

    private Connection connection;
    public String host, database, username, password;
    public int port;

    public ArrayList<Player> vanishedPlayers = new ArrayList<Player>();
    public ArrayList<Player> staffModePlayers = new ArrayList<Player>();
    public HashMap<Player, AdminInv> adminPlayerHashmap = new HashMap<>();

    
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
        getCommand ("spawn").setExecutor (new Spawn());
        getCommand ("livrebeta").setExecutor (new LivreBeta());
        getCommand("Vanish").setExecutor(new Vanich());
        getCommand("tpa").setExecutor(new tpa());
        getCommand("tpyes").setExecutor(new tpyes());
        getCommand("tpno").setExecutor(new tpno());

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
