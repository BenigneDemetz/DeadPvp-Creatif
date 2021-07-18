package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class ChatListeners extends ChatUtils implements Listener {


    @EventHandler
    public void onChat(PlayerChatEvent e){
        String msg = e.getMessage();
        Player p = e.getPlayer();
        if (msg.equals("[event cancelled by LiteBans")) return;

        /*ANTI MAJ */
        if(getUpperCaseNumber(msg) > 5 && !p.hasPermission("chat.admin")){
            msg = msg.toLowerCase();
        }

        /* COLOR CHAT */
        if(e.getPlayer().hasPermission("chat.apprenti")) msg = msg.replace("&", "§");

        /*ANTI LINK*/
        if(containLink(msg) && !p.hasPermission("chat.builer")){
            e.setCancelled(true);
            p.sendMessage("§cVous ne pouvez pas mettre de lien dans le chat");
        }


        //String[] insultes = {"tg","con","connard","pd","enculer","ez","pute","enculé"};
        //for(String insulte : insultes){
        //if(msg.toLowerCase().contains(insulte)){
        //msg = msg.replace(insulte,"§k"+insulte+"§f");
        //    }
        //}

        /* ♥ */
        if(msg.contains("<3") && (e.getPlayer().hasPermission("chat.apprenti"))){
            msg = msg.replace("<3","§c❤§f");
        }

        /*STAFF CHAT*/

        if(isStaffChat(msg) && p.hasPermission("chat.builder")){
            e.setCancelled(true);
            for (Player pls : Bukkit.getOnlinePlayers()){
                if(msg.startsWith("!!") && pls.hasPermission("chat.dev")){
                    pls.sendMessage(staffChat(msg, e.getPlayer()));
                } else if (msg.startsWith("!") && pls.hasPermission("chat.builder")){
                    pls.sendMessage(staffChat(msg, e.getPlayer()));
                }
            }
        }

        /*ANTI SPAM*/
        if (isInCooldown(p) && !p.hasPermission("chat.builder")) {
            e.setCancelled(true);
            p.sendMessage("§4tu dois attendre entre chaque message");
            return;
        } else {
            setCooldown(p, 1);
        }

        /*DOUBLE MSG*/
        if (hasAlreadySentMessage(p, msg) && !p.hasPermission("chat.builder")) {
          p.sendMessage("§4Vous ne pouvez pas envoyer 2 fois le meme message");
          e.setCancelled(true);
        } else {
            lastPlayerMessage.put(p, msg);
        }

        /* Bienvenue */
        if(canSayWelcome(p) && e.getMessage().toLowerCase().contains("bienvenue")){
            addKarmaAndList(p);
            if(p.hasPermission("chat.apprenti")) msg = colorBienvenue(msg);
        }

        /*MENTION*/
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (e.getMessage().contains(players.getName()) || e.getMessage().contains(players.getDisplayName())) {
                msg = msg.replace(players.getDisplayName(), "§b§l"+players.getDisplayName()+"§f");
                players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            } else if (e.getMessage().contains("@everyone") && p.hasPermission("chat.builder")){
                msg = msg.replace("@everyone","§c§l@everyone§f");
                players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            }
        }
        msg = msg.replace("%", "/100");
        e.setFormat(getPrefix(p) + p.getDisplayName() + ": §f" + msg);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        if(e.getMessage().startsWith("/minecraft:op") || e.getMessage().startsWith("/op")){
            e.getPlayer().sendMessage("§cCommande désactivée !");
            e.setCancelled(true);
            return;
        }
        Player p = e.getPlayer();

        if (e.getMessage().contains("/msg")) e.setCancelled(true);

        if(isLockedCommand(e.getMessage() ,getBlockedCommands()) && !p.hasPermission("chat.dev")) {
            e.setCancelled(true);
            p.sendMessage("§fCommande inconnue.");
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getMessage().contains("clear")) {
                    p.getInventory().setItem(8, PlayerListeners.book());
                }
            }
        }.runTaskLater(Main.getInstance(), 5L);

    }
}
