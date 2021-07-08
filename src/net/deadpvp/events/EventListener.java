package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.utils.BookUtils;
import net.deadpvp.utils.GuiUtils;
import net.deadpvp.utils.ItemBuilder;
import net.deadpvp.utils.sqlUtilities;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.print.Book;
import java.util.*;

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


        Location spawn = new Location(Bukkit.getServer().getWorld("Creatif"), 0.5,65.1,80.5, 0, 0);
        p.teleport(spawn);
        p.setPlayerListName(getPrefix(p)+p.getName());
        Player player = e.getPlayer();
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
        else hasAccepted.add(p);


        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("§4§lDEAD§9§lPVP");
        meta.setTitle("§dCarnet de commandes");
        book.setItemMeta(meta);

        p.getInventory().setItem(8, book);
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
        if (!hasAccepted.contains(e.getPlayer())) {
            BookUtils.openBook(book, e.getPlayer());
            e.getPlayer().sendMessage("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
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
        for(int i=0;i < 4;i++){
            if(e.getLine(i).toLowerCase().contains("http") || e.getLine(i).toLowerCase().contains("www") || e.getLine(i).toLowerCase().contains("://")){
                if(!e.getLine(i).toLowerCase().contains("http://deadpvp.com/")){
                    e.setLine(3,"§4LIEN INTERDIT ");
                    e.setLine(1,"§4§lhttps://youtu.be/");
                    e.setLine(2,"§4§ldQw4w9WgXcQ");
                    e.setLine(3,"§d§l§kDDDDDDDDD");
                }
            }
        }

    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){

        if(!hasAccepted.contains (e.getPlayer ())){
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

        if(msg.contains("http") || msg.contains("https")){
            if (!msg.contains("http://deadpvp.com/") && !msg.contains("https://deadpvp.com/")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cIl est interdit d'envoyer des lien sur §4§lDEAD§1§lPVP §c§l !");
                return;
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
            msg = msg.replace("<3","§c❤§f");
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
                if (!p.hasPermission("chat.builder")) {
                    p.sendMessage("§cErreur : tu dois attendre entre chaque message !");
                    e.setCancelled(true);
                    return;
                }
            }

        }
        Long c = System.currentTimeMillis() + (3 * 1000);
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
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() != null) {
            if (e.getItem().getType().equals(Material.WRITTEN_BOOK)) {
                BookMeta meta = (BookMeta) e.getItem().getItemMeta();
                if (meta.getTitle().contains("§dCarnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                    e.setCancelled(true);
                    p.openInventory(GuiUtils.mainMenu(p));
                }
            }
            if(e.getClickedBlock() == null)return;
            if (Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.AIR)) return;
            if(Objects.requireNonNull(e.getClickedBlock()).getBlockData().getMaterial() == Material.BEACON && e.getAction() == e.getAction().RIGHT_CLICK_BLOCK){
                e.setCancelled(true);
            }

            if (e.getItem() == null || e.getClickedBlock() == null) {
                return;
            }
            if((e.getClickedBlock().getBlockData().getMaterial() ==Material.END_PORTAL_FRAME && (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.ENDER_EYE
                    ||e.getPlayer().getInventory().getItemInOffHand().getType() == Material.ENDER_EYE))&& e.getAction() == e.getAction().RIGHT_CLICK_BLOCK){
                e.setCancelled(true);
                return;
            }
        }
    }
    @EventHandler
    public void onInteractInventory (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) return;

        if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) e.getCurrentItem().getItemMeta();
            if (meta.getTitle().contains("§dCarnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                e.setCancelled(true);
                p.openInventory(GuiUtils.mainMenu(p));
            }
        } //clic livre in inventory

        if (e.getView().getTitle().equals("§bCommandes")) {
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()) {
                case MAP:
//                    resetItemsGamemode(p);
                    p.setGameMode(GameMode.ADVENTURE);
                    p.openInventory(GuiUtils.mainMenu(p));
                    break;
                case GLASS:
                    p.setGameMode(GameMode.SPECTATOR);
                    p.openInventory(GuiUtils.mainMenu(p));
                    break;
                case IRON_SWORD:
//                    resetItemsGamemode(p);
                    p.setGameMode(GameMode.SURVIVAL);
                    p.openInventory(GuiUtils.mainMenu(p));
//                    p.getOpenInventory().setItem(3*9+7, GuiUtils.survieItem(true));
                    break;
                case CRAFTING_TABLE:
//                    resetItemsGamemode(p);
                    p.setGameMode(GameMode.CREATIVE);
                    p.openInventory(GuiUtils.mainMenu(p));
//                    p.getOpenInventory().setItem(3*9+6,GuiUtils.creaItem(true));
                    break;
                case GRASS:
                    p.performCommand("");
                    break;
                case OAK_DOOR:
                    p.performCommand("p home");
                    break;
                case NETHER_STAR:
                    p.performCommand("gadgetsmenu menu main");
                    break;
                case ENDER_CHEST:
                    p.performCommand("spawn");
                    break;
                case END_PORTAL_FRAME:
                    p.performCommand("hub");
                    break;

            }

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


    @EventHandler
    public void createPortal(PortalCreateEvent e) {
        e.setCancelled(true);
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

        if(e.getPlayer().hasPlayedBefore() && !hasAccepted.contains (e.getPlayer ()) && !e.getMessage ().equals ("/dpaccept")){
            BookUtils.openBook (book, e.getPlayer ());
            e.setCancelled (true);
            e.getPlayer ().sendMessage ("§c§lMerci de bien vouloir accepter les régles du créatif avant de pouvoir jouer.");
        }
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

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage("§c[§4-§d] " + getPrefix(e.getPlayer())+e.getPlayer().getName());;
    }

    public static String getPrefix(Player p) {
        if (p.getName().equals("Red_Spash")) return ChatColor.RED+"[Développeur] ";
        if (p.getName().equals("Arnaud013")) return ChatColor.RED+"[Développeur] ";
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
        if (p.getName().equals("Red_Spash")) return "§c";
        if (p.getName().equals("Arnaud013")) return "§c";
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