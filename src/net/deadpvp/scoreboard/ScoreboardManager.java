package net.deadpvp.scoreboard;

import net.deadpvp.commands.Vanich;
import net.deadpvp.utils.ChatUtils;
import net.deadpvp.utils.sqlUtilities;
import net.minecraft.server.v1_16_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.TimeZone;

public class ScoreboardManager implements Runnable{
    public static int TICK_COUNT= 0;
    public static long[] TICKS= new long[600];
    public static long LAST_TICK= 0L;

    /*
    * TODO: opti classe
    * */

    public static void setScoreBoard(Player player) {
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        Date date = new Date();
        int x = (22-date.getHours())-1;
        int y = 59-date.getMinutes();
        String y2;
        if (y < 10){
            int temp = y;
            y2 = "0"+y;
        }else{
            y2 = ""+y;
        }
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("§d§lCREATIF", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score15 = obj.getScore("§c§l§r");
        Score score14 = obj.getScore("§b§lVOTRE PROFIL");
        Score score11 = obj.getScore("§f§2 ");
        Score score10 = obj.getScore("§c§lSERVEUR");
        Score score8 = obj.getScore("§c§l§c§r");

        if(player.hasPermission("chat.dev")){
            Score score7 = obj.getScore("§4§lADMIN");
            score7.setScore(7);
            Score score3 = obj.getScore("§7§l§c");
            score3.setScore(3);
        }

        //Score score3 = obj.getScore("§dEn cas de bug faites");
        //Score score2 = obj.getScore("§d/bug <votre bug> !");
        Score score1 = obj.getScore("§c§b§l---------------§r");
        Score score0 = obj.getScore("§b§lmc.deadpvp.com");

        score0.setScore(0);
        score1.setScore(1);
        //score2.setScore(2);
        //score3.setScore(3);
        score8.setScore(8);



        score11.setScore(11);
        score15.setScore(15);

        score10.setScore(10);

        score14.setScore(14);

        Team Mystiques = board.registerNewTeam("Mystiques");
        Mystiques.addEntry(ChatColor.GOLD + "" + ChatColor.GOLD);
        Mystiques.setPrefix(ChatColor.WHITE+"§f≫ Mystique");
        try {
            Object mystiqueint = sqlUtilities.getData("moneyserv","player",player.getName(),"mystiques","Int");
            Mystiques.setSuffix("§fs: §d"+ mystiqueint.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        obj.getScore(ChatColor.GOLD + "" + ChatColor.GOLD).setScore(12);


        Team Karma = board.registerNewTeam("Karma");
        Karma.addEntry(ChatColor.LIGHT_PURPLE + "" + ChatColor.DARK_PURPLE);
        Karma.setPrefix(ChatColor.WHITE+"§f≫ Karma: ");
        try {
            Object karmaint = sqlUtilities.getData("moneyserv","player",player.getName(),"karma","Int");
            Karma.setSuffix("§d"+karmaint.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        };
        obj.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.DARK_PURPLE).setScore(11);

        Team grade= board.registerNewTeam("grade");
        grade.addEntry(ChatColor.GOLD + "" + ChatColor.LIGHT_PURPLE);
        grade.setPrefix(ChatColor.WHITE+"≫ Grade: ");
        String prefix = ChatUtils.getPrefix(player).replace("[","");

        grade.setSuffix(prefix.replaceAll("]","")+"");
        obj.getScore(ChatColor.GOLD + "" + ChatColor.LIGHT_PURPLE).setScore(13);

        Team onlineCounter = board.registerNewTeam("onlineCounter");
        onlineCounter.addEntry(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC);
        onlineCounter.setPrefix(ChatColor.WHITE+"≫ Créatif: ");
        int nbrjoueur = Bukkit.getOnlinePlayers().size()- Vanich.inVanish.size();
        String phrase = "";
        if(nbrjoueur >1){
            phrase = "§6"+nbrjoueur+" joueurs";
        }else{
            phrase = "§6"+nbrjoueur+" joueur";
        }
        onlineCounter.setSuffix(phrase);
        obj.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC).setScore(9);

        if(player.hasPermission("chat.dev")){
            Team tps= board.registerNewTeam("tps");
            tps.addEntry(ChatColor.BLACK + "" + ChatColor.LIGHT_PURPLE);
            tps.setPrefix(ChatColor.WHITE+"≫ TPS: "+getTPS(100));
            obj.getScore(ChatColor.BLACK + "" + ChatColor.LIGHT_PURPLE).setScore(6);
        }




        player.setScoreboard(board);
    }
    public static void updateScoreBoard(Player player){
        Scoreboard board = player.getScoreboard();
        if(player.hasPermission("chat.dev")){
            String tps =getTPS(100);
            board.getTeam("tps").setPrefix(ChatColor.WHITE+"≫ TPS: "+tps);
        }
        try {
            Object karmaint = sqlUtilities.getData("moneyserv","player",player.getName(),"karma","Int");
            String karma = "§d"+karmaint.toString();
            board.getTeam("Karma").setSuffix(karma);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Object mystiqueint = null;
        try {
            mystiqueint = sqlUtilities.getData("moneyserv","player",player.getName(),"mystiques","Int");
            board.getTeam("Mystiques").setSuffix("§fs: §d"+ mystiqueint.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        board.getTeam("grade").setSuffix(ChatUtils.getPrefix(player)+"");
        int nbrjoueur = Bukkit.getOnlinePlayers().size()- Vanich.inVanish.size();
        String phrase = "";
        if(nbrjoueur >1){
            phrase = "§6"+nbrjoueur+" joueurs";
        }else{
            phrase = "§6"+nbrjoueur+" joueur";
        }
        board.getTeam("onlineCounter").setSuffix(phrase);

    }


    public static String getTPS(int ticks)
    {
        double[] tps = MinecraftServer.getServer().recentTps;
        double raw = tps[0];
        double ftps = (double) Math.min(Math.round(raw * 100.0) / 100.0, 20.0);
        BigDecimal bigtps = BigDecimal.valueOf(ftps);
        bigtps = bigtps.setScale(2);
        int fin = bigtps.intValue();
        if(fin==20){
            return "§a20.00";
        }else if(fin >= 18){
            return "§2"+fin;
        }else if(fin >=15){
            return "§6"+fin;
        }else if(fin >=10){
            return "§c"+fin;
        }else{
            return "§4⚠ "+fin;
        }


//
    }

    @Override
    public void run() {

        if(Bukkit.getOnlinePlayers().size() == 0){
            return;
        }else{
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.getScoreboard() == null){
                    setScoreBoard(p);
                }
                updateScoreBoard(p);
            }
        }

    }


}
