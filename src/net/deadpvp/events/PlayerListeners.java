package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.commands.Vanich;
import net.deadpvp.gui.guis.MainGui;
import net.deadpvp.scoreboard.ScoreboardManager;
import net.deadpvp.utils.AdminInv;
import net.deadpvp.utils.ChatUtils;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerListeners implements Listener {

    public Team admin;
    public Team dev;
    public Team modo;
    public Team builder;
    public Team joueur;
    public Team architecte;
    public Team constructeur;
    public Team apprenti;

    public Location spawn = new Location(Bukkit.getServer().getWorld("Creatif"), 0.5, 65.1, 80.5, 0, 0);


    static ArrayList<Player> punishedPlayers= new ArrayList<>();

    public static ItemStack book(){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("§4§lDEAD§9§lPVP");
        meta.setTitle("§dCarnet de commandes");
        book.setItemMeta(meta);
        return book;
    }

    /*
    * TODO: opti classe
    * */

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ScoreboardManager.setScoreBoard(p);
        settab(p);
        p.getInventory().setItem(8, book());

        if (!p.hasPermission("deadpvp.vanich")) {
            for (Player playervanished : Vanich.inVanish) {
                p.hidePlayer(playervanished);
            }
        }

        p.teleport(spawn);
        p.setPlayerListName(ChatUtils.getPrefix(p) + p.getDisplayName());

        e.setJoinMessage("§a[§4+§a] " + ChatUtils.getPrefix(p) + e.getPlayer().getDisplayName());
        e.getPlayer().setGameMode(GameMode.CREATIVE);

        if (!p.hasPlayedBefore()) {
            Bukkit.broadcastMessage("§7Souhaitez la bienvenue à §6" + p.getName() + "§7 pour reçevoir une récompense !");
            ChatListeners.bienvenue = true;
            ChatListeners.hasSaidBienvenue.clear();
            p.sendMessage("§6Bienvenue sur le créatif de §4§lDEAD§1§lPVP §6!\n§6Pour créer un plot faites la commande /plot auto ou /plot claim sur un plot qui est vide.\nEn cas de problème contactez le staff sur §9discord: discord.gg/23kPxkbzDg");
            new BukkitRunnable() {
                @Override
                public void run() {
                    ChatListeners.bienvenue = false;
                }
            }.runTaskLater(Main.getInstance(), 300);
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(e.getPlayer().getLocation().getY() <= 0){
            Player p = e.getPlayer();
            if(p.getBedSpawnLocation() == null){
                p.teleport(spawn);
                p.sendMessage("§7Vous venez d'être téléporté au spawn !");
            }else{
                p.sendMessage("§7Vous venez d'être téléporté à votre point de réapparition !");
                p.teleport(p.getBedSpawnLocation());
            }
        }
        if (Main.freeze.contains(e.getPlayer())) {
            e.setCancelled(true);
            e.getPlayer().sendTitle("§c§lVous êtes freeze !", "§6§lMerci de venir sur discord: discord.gg/23kPxkbzDg", 1000, 0, 0);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() != null) {
            if(itemWithCommand(p.getItemOnCursor(),p,e.getClickedBlock()) || itemWithCommand(e.getItem(),p,e.getClickedBlock())){
                //On doit verifier comme ça sinon la hache de worldedit casse les blocks
                e.setCancelled(true);
            }
            if (e.getItem().getType().equals(Material.WRITTEN_BOOK)) {
                BookMeta meta = (BookMeta) e.getItem().getItemMeta();
                if (meta == null) return;
                if (!meta.hasTitle()) return;
                if (!meta.hasAuthor()) return;

                if (meta.getTitle().contains("§dCarnet de commandes") && meta.getAuthor().equals("§4§lDEAD§9§lPVP")) {
                    e.setCancelled(true);
                    MainGui mainGui = new MainGui(Main.getPlayerGuiUtils(p));
                    mainGui.openInv();
                }
            }
            if (e.getClickedBlock() == null) return;
            if (Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.AIR)) return;
            if (Objects.requireNonNull(e.getClickedBlock()).getBlockData().getMaterial() == Material.BEACON && e.getAction() == e.getAction().RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
            }

            if (e.getItem() == null || e.getClickedBlock() == null) {
                return;
            }
            if ((e.getClickedBlock().getBlockData().getMaterial() == Material.END_PORTAL_FRAME && (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.ENDER_EYE
                    || e.getPlayer().getInventory().getItemInOffHand().getType() == Material.ENDER_EYE)) && e.getAction() == e.getAction().RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage("§d[§4-§d] " + ChatUtils.getPrefix(e.getPlayer()) + e.getPlayer().getName());
        ;
        Player player = e.getPlayer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }
        if (Vanich.inVanish.contains(e.getPlayer())) {
            AdminInv ai = AdminInv.getFromPlayer(player);
            ai.destroy();
            Main.getInstance().staffModePlayers.remove(player);
            ai.giveInv(player);
            player.sendMessage("§bVous n'êtes plus en vanish !");
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setPlayerListName(ChatUtils.getPrefix(player) + player.getName());
            Vanich.inVanish.remove(player);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(p.getHealth() <= e.getDamage() && !e.isCancelled()){
                e.setCancelled(true);
                p.sendTitle("§f","§cVous êtes mort !",20,20*3,1);
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setFallDistance(0);
                if(p.getBedSpawnLocation() == null){
                    p.teleport(spawn);
                }else{
                    p.teleport(p.getBedSpawnLocation());
                }
                p.setFallDistance(0);

                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) book.getItemMeta();
                meta.setAuthor("§4§lDEAD§9§lPVP");
                meta.setTitle("§dCarnet de commandes");
                book.setItemMeta(meta);

                p.getInventory().setItem(8, book);
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

    public static boolean itemWithCommand (ItemStack itemToUse, Player p) {
        net.minecraft.server.v1_16_R1.ItemStack item = CraftItemStack.asNMSCopy(itemToUse);
        if (item.hasTag()) {
            NBTTagCompound nbt = item.getTag();
            if ((nbt.toString()).contains("run_command")) {
                System.out.println("§c" + p.getName() + " A TENTE DE METTRE UNE COMMANDE SUR UN ITEM : " + nbt.toString());
                p.getInventory().clear();
                p.getInventory().setItem(8, book());
                p.closeInventory();
                punishRoom(p);
                return true;
            }
        }
        return false;
    }
    public static boolean itemWithCommand (ItemStack itemToUse, Player p, Block block) {
        net.minecraft.server.v1_16_R1.ItemStack item = CraftItemStack.asNMSCopy(itemToUse);
        if (item.hasTag()) {
            NBTTagCompound nbt = item.getTag();
            if ((nbt.toString()).contains("run_command")) {
                System.out.println("§c" + p.getName() + " A TENTE DE METTRE UNE COMMANDE SUR UN ITEM : " + nbt.toString());
                p.getInventory().clear();
                p.getInventory().setItem(8, book());
                punishRoom(p);
                return true;
            }
        }
        return false;
    }

    public static void punishRoom (Player p) {
//        p.teleport(new Location(Bukkit.getWorld("Creatif"),0.5,20.1,0.5,0,0));
//        punishedPlayers.add(p);
//        p.setGameMode(GameMode.SURVIVAL);
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                p.damage(5);
//            }
//        }.runTaskTimer(Main.getInstance(),0,5);
    }

    public void settab(Player p){
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        for(Player update: Bukkit.getOnlinePlayers()){
            Scoreboard board = teaminit(update);
            for(Player pl : Bukkit.getOnlinePlayers()){
                setteamto(pl);
            }
            update.setScoreboard(board);
        }





    }

    public Scoreboard teaminit(Player p){
        Scoreboard board = p.getScoreboard();
        admin = board.getTeam("001-Admin");
        if(admin == null){
            admin = board.registerNewTeam("001-Admin");
            admin.setPrefix("§4[Admin] ");
        }

        dev = board.getTeam("002-Dev");
        if(dev == null){
            dev = board.registerNewTeam("002-Dev");
            dev.setPrefix("§c[Développeur] ");
        }

        modo = board.getTeam("003-modo");
        if(modo == null){
            modo = board.registerNewTeam("003-modo");
            modo.setPrefix("§6[Modérateur] ");
        }

        builder = board.getTeam("004-builder");
        if(builder == null){
            builder = board.registerNewTeam("004-builder");
            builder.setPrefix("§9[Builder] ");
        }

        architecte = board.getTeam("005-architecte");
        if(architecte == null){
            architecte = board.registerNewTeam("005-architecte");
            architecte.setPrefix("§b[Architecte] ");
        }

        constructeur = board.getTeam("006-constructeur");
        if(constructeur == null){
            constructeur = board.registerNewTeam("006-constructeur");
            constructeur.setPrefix("§3[Constructeur] ");
        }

        apprenti = board.getTeam("007-apprenti");
        if(apprenti == null){
            apprenti = board.registerNewTeam("007-apprenti");
            apprenti.setPrefix("§a[Apprenti] ");
        }


        joueur = board.getTeam("008-joueur");
        if(joueur == null){
            joueur = board.registerNewTeam("008-joueur");
            joueur.setPrefix("§7");
        }
        return board;
    }
    public void setteamto(Player pl){
        if(pl.getPlayer().getName().equalsIgnoreCase("Red_Spash") || pl.getPlayer().getName().equalsIgnoreCase("Arnaud013")){
            pl.setPlayerListName(dev.getPrefix()+pl.getPlayer().getName());
            dev.addPlayer(pl);
            dev.addEntry(pl.getName());
        }else if(pl.hasPermission("chat.admin")){
            pl.setPlayerListName(admin.getPrefix()+pl.getPlayer().getName());
            admin.addPlayer(pl);
            admin.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.dev")){
            pl.setPlayerListName(dev.getPrefix()+pl.getPlayer().getName());
            dev.addPlayer(pl);
            dev.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.modo")){
            pl.setPlayerListName(modo.getPrefix()+pl.getPlayer().getName());
            modo.addPlayer(pl);
            modo.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.builder")){
            pl.setPlayerListName(builder.getPrefix()+pl.getPlayer().getName());
            builder.addPlayer(pl);
            builder.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.architecte")){
            pl.setPlayerListName(architecte.getPrefix()+pl.getPlayer().getName());
            architecte.addPlayer(pl);
            architecte.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.constructeur")){
            pl.setPlayerListName(constructeur.getPrefix()+pl.getPlayer().getName());
            constructeur.addPlayer(pl);
            constructeur.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.apprenti")){
            pl.setPlayerListName(apprenti.getPrefix()+pl.getPlayer().getName());
            apprenti.addPlayer(pl);
            apprenti.addEntry(pl.getName());
        }else{
            pl.setPlayerListName(joueur.getPrefix()+pl.getPlayer().getName());
            joueur.addPlayer(pl);
            joueur.addEntry(pl.getName());
        }
    }
}
