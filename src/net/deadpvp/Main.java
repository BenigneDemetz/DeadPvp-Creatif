package net.deadpvp;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.deadpvp.commands.*;
import net.deadpvp.events.EventListener;
import net.deadpvp.utils.AdminInv;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin implements PluginMessageListener {
    private static Main instance;
    public int playerCount;
    private Connection connection;
    public String host, database, username, password;
    public int port;

    public ArrayList<Player> vanishedPlayers = new ArrayList<Player>();
    public ArrayList<Player> staffModePlayers = new ArrayList<Player>();
    public HashMap<Player, AdminInv> adminPlayerHashmap = new HashMap<>();
    public HashMap<String, String> nickname = new HashMap<>();

    
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
        getCommand ("nick").setExecutor (new Nick());
        getCommand ("freeze").setExecutor (new freeze());
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
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.ADD_PLAYER) return;
                List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<PlayerInfoData>();
                List<PlayerInfoData> playerInfoDataList = event.getPacket().getPlayerInfoDataLists().read(0);
                for (PlayerInfoData playerInfoData : playerInfoDataList) {
                    if (playerInfoData == null || playerInfoData.getProfile() == null || Bukkit.getPlayer(playerInfoData.getProfile().getUUID()) == null) { //Unknown Player
                        newPlayerInfoDataList.add(playerInfoData);
                        continue;
                    }
                    WrappedGameProfile profile = playerInfoData.getProfile();
                    profile = profile.withName(profile.getName());


                    if (nickname.containsKey(profile.getName())) profile =
                            profile.withName(Nick.getColor(Bukkit.getPlayer(profile.getName())) + nickname.get(profile.getName()));
                    PlayerInfoData newPlayerInfoData = new PlayerInfoData(profile, playerInfoData.getPing(), playerInfoData.getGameMode(), playerInfoData.getDisplayName());



                    newPlayerInfoDataList.add(newPlayerInfoData);
//                    if (nickname.containsKey(profile.getName()))
//                        Bukkit.getPlayer(profile.getName()).setPlayerListName(EventListener.getPrefixColor(Bukkit.getPlayer(profile.getName()))+ nickname.get(profile.getName()));

                }
                event.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfoDataList);
            }
        });
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
            System.out.println("[Boutique]");
            System.out.println(cmd);
            player.setPlayerListName(EventListener.getPrefix(player)+player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
        if (subchannel.equals ("PlayerCount")) {
            String server = in.readUTF();
            if (server.equalsIgnoreCase("all")) {
                playerCount = in.readInt();
            }
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
