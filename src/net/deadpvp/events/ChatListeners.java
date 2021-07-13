package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.utils.ChatUtils;
import net.deadpvp.utils.sqlUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatListeners implements Listener {

    public static Map<Player, Long> spam = new HashMap<Player, Long>();
    public static Map<Player, String> doublemsg = new HashMap<Player, String>();
    public static Map<Player, Boolean> saybienvenue = new HashMap<Player, Boolean>();
    public static Boolean bienvenue = false;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){

        String msg = e.getMessage();
        String temp;
        int maj = 0;
        Player p = e.getPlayer();
        if (msg.equals("[event cancelled by LiteBans")) return;

        /*ANTI MAJ
        *  30% maj max / message
        * */
        for (int k = 0; k < e.getMessage().length(); k++) {
            if (Character.isUpperCase(msg.charAt(k))) {
                maj++;
            }
        }
        if(!p.hasPermission("chat.admin") && maj > 5){
            msg = msg.toLowerCase(); //Methode toLowerCase, plus simple que de loop une nouvelle fois a travers chaque caractere pour les remmetre en minuscule
        }

        /* COLOR CHAT
        *  -feature payante
        * */
        if(e.getPlayer().hasPermission("chat.apprenti") || e.getPlayer().hasPermission("chat.builder"))
            msg = msg.replace("&", "§");

        char carac = '§';
        int count = 0;
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == carac) {
                count++;
            }
        }
        if (msg.length() <= count*2) e.setCancelled(true);

        /*ANTI LINK*/

        if(msg.contains("http") || msg.contains("https") ){
            if ((!msg.contains("http://deadpvp.com/") && !msg.contains("https://deadpvp.com/"))&& !e.getPlayer().hasPermission("chat.builder")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cIl est interdit d'envoyer des lien sur §4§lDEAD§1§lPVP §c§l !");
                return;
            }

        }

        //String[] insultes = {"tg","con","connard","pd","enculer","ez","pute","enculé"};
        //for(String insulte : insultes){
        //if(msg.toLowerCase().contains(insulte)){
        //msg = msg.replace(insulte,"§k"+insulte+"§f");
        //    }
        //}

        /* ♥ */
        if(msg.contains("<3") && (e.getPlayer().hasPermission("chat.apprenti") || e.getPlayer().hasPermission("chat.builder"))){
            msg = msg.replace("<3","§c❤§f");
        }

        /*STAFF CHAT*/
        if (msg.startsWith("!!") && (p.hasPermission("chat.admin") || p.hasPermission("chat.dev") || p.hasPermission("chat.modo"))) {
            msg = "§c[AdminChat] " + ChatUtils.getPrefixColor(e.getPlayer()) + e.getPlayer().getName() + "§6: " + e.getMessage();
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
            msg = "§d[StaffChat] " + ChatUtils.getPrefixColor(e.getPlayer()) + e.getPlayer().getName() + "§6: " + e.getMessage();
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

        /*ANTI SPAM*/
        if (spam.containsKey(p)) {
            if (spam.get(p) > System.currentTimeMillis()) {
                if (!p.hasPermission("chat.builder")) {
                    p.sendMessage("§cErreur : tu dois attendre entre chaque message !");
                    e.setCancelled(true);
                    return;
                }
            }

        }
        Long c = System.currentTimeMillis() + (1000);
        if (!(p.hasPermission("chat.dev") || p.hasPermission("chat.admin") || p.hasPermission("chat.modo") ||
                p.hasPermission("chat.builder"))) {
            spam.put(p, c);
        }
        if (e.getMessage().equalsIgnoreCase(doublemsg.get(p.getPlayer()))) {
            if (!p.hasPermission("chat.builder")) {
                p.sendMessage("§cErreur : impossible d'envoyer 2 fois le même message d'affilé !");
                e.setCancelled(true);
                return;
            }
        }

        /*MENTION*/
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (e.getMessage().contains(players.getName()) || e.getMessage().contains(players.getDisplayName())) {
                msg = msg.replace(players.getName(), "§b§l"+players.getDisplayName()+"§f");
                msg = msg.replace(players.getDisplayName(), "§b§l"+players.getDisplayName()+"§f");
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

        /*Bienvenue*/

        if(((e.getMessage().toLowerCase().contains("bienvenue") || e.getMessage().toLowerCase().contains("bienvenue!")) && bienvenue == true) && saybienvenue.get(p)== false){
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
                saybienvenue.put(p,true);
            }
            catch (Exception ee) {
                ee.printStackTrace();
            }

        }
        if((e.getMessage().toLowerCase().contains("bienvenue") || e.getMessage().toLowerCase().contains("bienvenue!")) && (e.getPlayer().hasPermission("chat.apprenti") || e.getPlayer().hasPermission("chat.builder"))){

            msg = msg.replaceAll("(?i)bienvenue","§6§lBienvenue§f");
            msg = msg.replaceAll("(?i)bienvenue!","§6§lBienvenue§f!");
        }

        e.setFormat(ChatUtils.getPrefix(p) + p.getDisplayName() + ": §f" + msg);

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();

        if (e.getMessage().contains("/msg")) e.setCancelled(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getMessage().contains("clear")) {
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta meta = (BookMeta) book.getItemMeta();
                    meta.setAuthor("§4§lDEAD§9§lPVP");
                    meta.setTitle("§dCarnet de commandes");
                    book.setItemMeta(meta);

                    p.getInventory().setItem(8, book);
                }
            }
        }.runTaskLater(Main.getInstance(), 5L);

        if (( e.getMessage().startsWith("/minecraft:list") || e.getMessage().startsWith("/list") || e.getMessage().startsWith("/pl")
                || e.getMessage().startsWith("/plugins") || e.getMessage().startsWith("/help")|| e.getMessage().startsWith("/bukkit:pl")
                || e.getMessage().startsWith("/bukkit:plugins") || e.getMessage().startsWith("/bukkit:?") || e.getMessage().startsWith("/?")
                || e.getMessage().startsWith("/bukkit:help") )){
            if(p.hasPermission("chat.builder"))return;
            if(e.getMessage().startsWith("/perm") && !p.hasPermission("chat.dev"))e.setCancelled(true);

            if(e.getMessage().startsWith("/plot") || e.getMessage().startsWith("/plugman") )return;


            e.setCancelled(true);
            e.getPlayer().sendMessage("§fCommande inconnue.");
        }
    }

}
