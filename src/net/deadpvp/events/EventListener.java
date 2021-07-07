package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.utils.BookUtils;
import net.deadpvp.utils.ItemBuilder;
import net.deadpvp.utils.sqlUtilities;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EventListener implements Listener {
    Boolean bienvenue = false;
    public static Map<Player,Player> lastdamage = new HashMap<Player,Player>();
    Map<Player, Long> spam = new HashMap<Player, Long>();
    Map<Player, String> doublemsg = new HashMap<Player, String>();
    public static ArrayList<Player> hasAccepted = new ArrayList<> ();
    private static final ItemStack book = BookUtils.createBook ();

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        e.getPlayer().setGameMode(GameMode.CREATIVE);
    }




    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer ();
        p.setPlayerListName(getPrefix(p)+p.getName());
        e.setJoinMessage ("§2[§4+§a] "+getPrefix(p) + e.getPlayer ().getDisplayName ());
        e.getPlayer ().setGameMode (GameMode.CREATIVE);
        new BukkitRunnable () {
            @Override
            public void run() {
                e.getPlayer ().setGameMode (GameMode.CREATIVE);
            }
        }.runTaskLater (Main.getInstance (), 20L);

        if(!p.hasPlayedBefore ()) {
            BookUtils.openBook (book, p);
        }

        ItemBuilder menu = new ItemBuilder(Material.WRITTEN_BOOK).setName("§dMenu");
        p.getInventory().setItem(7, menu.toItemStack());

        if(!p.hasPlayedBefore()){
            Bukkit.broadcastMessage("§7Souhaitez la bienvenue à §6"+p.getName()+"§7 pour reçevoir une récompense !");
            bienvenue = true;
            p.sendMessage("§6Bienvenue sur le créatif de §4§lDEAD§1§lPVP §6!\n§6Pour créer un plot faites la commande /plot auto ou /plot claim sur un plot qui est vide.\nEn cas de problème contactez le staff sur §9discord: discord.gg/23kPxkbzDg");
            new BukkitRunnable() {
                @Override
                public void run() {
                    bienvenue = false;
                }
            }.runTaskLater(Main.getInstance(), 300);
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

    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){

        if(!e.getPlayer().hasPlayedBefore() && !hasAccepted.contains (e.getPlayer ())){
            BookUtils.openBook (book, e.getPlayer ());
            e.setCancelled (true);
            e.getPlayer ().sendMessage ("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
            return;
        }

        String msg = e.getMessage();
        int maj = 0;
        int max = 1;
        String msgsans = msg;
        for (int k = 0; k < e.getMessage().length(); k++) {
            if (Character.isUpperCase(msg.charAt(k))) {
                maj++;
                char temp = Character.toLowerCase(e.getMessage().charAt(k));
                char x = e.getMessage().charAt(k);
                max++;
                msgsans.replace(x, Character.toLowerCase(x));
            }
        }
        int pourcentage = (maj * 100) / max;
        if (pourcentage >= 30) {
            msg = msgsans;
        }
        //String[] insultes = {"tg","con","connard","pd","enculer","ez","pute","enculé"};
        //for(String insulte : insultes){
            //if(msg.toLowerCase().contains(insulte)){
                //msg = msg.replace(insulte,"§k"+insulte+"§f");
                //    }
        //}
        if(msg.contains("<3") && (e.getPlayer().hasPermission("chat.apprenti") || e.getPlayer().hasPermission("chat.builder"))){
            msg = msg.replace("<3","§c❤");
        }
        Player p = e.getPlayer();

        if (msg.startsWith("!!") && (p.hasPermission("chat.admin") || p.hasPermission("chat.dev") || p.hasPermission("chat.modo"))) {
            msg = "§c[AdminChat] " + getPrefixColor(e.getPlayer()) + e.getPlayer().getName() + "§6: " + e.getMessage();
            msg = msg.replaceFirst("!!", "");
            e.setCancelled(true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("chat.admin") || player.hasPermission("chat.dev")) {

                    player.sendMessage(msg + "");
                }
            }
            return;
        }
        if (msg.startsWith("!") && (p.hasPermission("chat.admin") || p.hasPermission("chat.dev") || p.hasPermission("chat.modo"))) {
            msg = "§d[StaffChat] " + getPrefixColor(e.getPlayer()) + e.getPlayer().getName() + "§6: " + e.getMessage();
            msg = msg.replaceFirst("!", "");
            e.setCancelled(true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("chat.admin") || player.hasPermission("chat.dev") || player.hasPermission("chat.modo") ||
                        player.hasPermission("chat.builder")) {
                    player.sendMessage(msg + "");
                }
            }
            return;
        }


        if (msg.equals("[event cancelled by LiteBans")) return;
        if (spam.containsKey(p)) {
            if (spam.get(p) > System.currentTimeMillis()) {
                p.sendMessage("§cErreur : tu dois attendre entre chaque message !");
                e.setCancelled(true);
                return;
            }

        }
        Long c = System.currentTimeMillis() + (3 * 1000);
        if (!(p.hasPermission("chat.dev") || p.hasPermission("chat.admin") || p.hasPermission("chat.modo") ||
                p.hasPermission("chat.builder"))) {
            spam.put(p, c);
        }
        if (e.getMessage().equalsIgnoreCase(doublemsg.get(p.getPlayer()))) {
            p.sendMessage("§cErreur : impossible d'envoyer 2 fois le même message d'affilé !");
            e.setCancelled(true);
            return;
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (e.getMessage().contains(players.getName())) {

                msg = msg.replace(players.getName(), "§b§l"+players.getName()+"§f");
                players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            }
        }
        if(e.getMessage().contains("@everyone") && (e.getPlayer().hasPermission("chat.admin") || e.getPlayer().hasPermission("chat.dev") || e.getPlayer().hasPermission("chat.modo") || e.getPlayer().hasPermission("chat.builder")) ){
            for(Player player2 : Bukkit.getOnlinePlayers()){
                msg = msg.replace("@everyone","§c§l@everyone§f");

                player2.playSound(player2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            }

        }
        doublemsg.put(e.getPlayer(), e.getMessage());



        if((e.getMessage().toLowerCase().contains("bienvenue") || e.getMessage().toLowerCase().contains("bienvenue!")) && bienvenue == true){
            Random r = new Random();
            int karmatogive = r.nextInt(4) + 1;
            try {
                if (sqlUtilities.hasData("moneyserv", "player", p.getName())) {
                    int data = (int) sqlUtilities.getData("moneyserv","player",p.getName(), "karma","Int" );
                    sqlUtilities.updateData("moneyserv", "karma", data + karmatogive, p.getName());
                }
                else {
                    sqlUtilities.insertData("moneyserv", p.getName(), 0, karmatogive, "player, mystiques, karma");
                }
                e.getPlayer().sendMessage("§d+"+karmatogive+" Karma");
            }
            catch (Exception ee) {
                ee.printStackTrace();
            }

        }
        if((e.getMessage().toLowerCase().contains("bienvenue") || e.getMessage().toLowerCase().contains("bienvenue!")) && (e.getPlayer().hasPermission("chat.apprenti") || e.getPlayer().hasPermission("chat.builder"))){

            msg = msg.replaceAll("(?i)bienvenue","§6§lBienvenue§f");
            msg = msg.replaceAll("(?i)bienvenue!","§6§lBienvenue§f!");
        }

        e.setFormat(getPrefix(p) + p.getName() + ": §f" + msg);
    }

    @EventHandler
    public void OnCreaturespawn(CreatureSpawnEvent e){
        if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN
        ||e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_WITHER || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM){
            e.setCancelled(true);
        }

    }
    @EventHandler
    public void rightclick(PlayerInteractEvent e) {
        if(e.getHand() == null){
            return;
        }
        if(e.getAction() == null){
            return;
        }
        if(e.getMaterial() == null){
            return;
        }
        if(e.getClickedBlock().getBlockData().getMaterial() == Material.BEACON){
            e.setCancelled(true);
        }

        if (e.getPlayer().getInventory().getItemInOffHand().getType() == null || e.getPlayer().getInventory().getItemInMainHand().getType() == null) {
            return;
        }
        if(e.getClickedBlock().getBlockData().getMaterial() == null){
            return;
        }
        if((e.getClickedBlock().getBlockData().getMaterial() ==Material.END_PORTAL_FRAME && (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.ENDER_EYE
                ||e.getPlayer().getInventory().getItemInOffHand().getType() == Material.ENDER_EYE))&& e.getAction() == Action.RIGHT_CLICK_BLOCK){
            e.setCancelled(true);
            return;

        }
    }

    @EventHandler
    public void rightclick(PortalCreateEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        if(e.getPlayer().hasPlayedBefore() && !hasAccepted.contains (e.getPlayer ()) && !e.getMessage ().equals ("/dpaccept")){
            BookUtils.openBook (book, e.getPlayer ());
            e.setCancelled (true);
            e.getPlayer ().sendMessage ("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
        }
        if (( e.getMessage().startsWith("/minecraft:list") || e.getMessage().startsWith("/list") || e.getMessage().startsWith("/pl")
                || e.getMessage().startsWith("/plugins") || e.getMessage().startsWith("/help")|| e.getMessage().startsWith("/bukkit:pl")
                || e.getMessage().startsWith("/bukkit:plugins") || e.getMessage().startsWith("/bukkit:?") || e.getMessage().startsWith("/?")
                || e.getMessage().startsWith("/bukkit:help"))){
            if(e.getMessage().startsWith("/plot") || e.getMessage().startsWith("/plugman") ){
                return;
            }
            e.setCancelled(true);
            e.getPlayer().sendMessage("§fCommande inconnue.");
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage("§c[§4-§d] " + getPrefix(e.getPlayer())+e.getPlayer().getName());;
    }

    public static String getPrefix(Player p) {
        if (p.getName() == "Red_Spash") return ChatColor.RED+"[Développeur] ";
        if (p.hasPermission("chat.admin")) return ChatColor.DARK_RED+"[Administrateur] ";
        if (p.hasPermission("chat.dev")) return ChatColor.RED+"[Développeur] ";
        if (p.hasPermission("chat.modo")) return ChatColor.GOLD+"[Modérateur] ";
        if (p.hasPermission("chat.builder")) return ChatColor.BLUE+"[Builder] ";
        if (p.hasPermission("deadpvp.architecte")) return "§b[Architecte] §b";
        if (p.hasPermission("deadpvp.constructeur")) return "§3[Constructeur] §3";
        if (p.hasPermission("deadpvp.apprenti")) return "§a[Apprenti] §a";


        else return "§7";
    }

    public static String getPrefixColor(Player p) {
        if (p.hasPermission("chat.admin")) return "§4";
        if (p.hasPermission("chat.dev")) return "§c";
        if (p.hasPermission("chat.modo")) return "§6";
        if (p.hasPermission("chat.builder")) return "§9";
        if (p.hasPermission("chat.vip")) return "§b";
        if (p.hasPermission("deadpvp.architecte")) return "§b";
        if (p.hasPermission("deadpvp.constructeur")) return "§3";
        if (p.hasPermission("deadpvp.apprenti")) return "§a";


        else return "§7";
    }
    
}
