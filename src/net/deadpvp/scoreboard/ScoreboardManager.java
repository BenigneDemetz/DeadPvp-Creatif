package net.deadpvp.scoreboard;

import net.deadpvp.Main;
import net.deadpvp.utils.ChatUtils;
import net.deadpvp.utils.sqlUtilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.TimeZone;

public class ScoreboardManager {

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
        Score score6 = obj.getScore("§f§l§c");
        if(player.hasPermission("chat.dev")){
            Score score7 = obj.getScore("§4§lADMIN");
        }

        Score score3 = obj.getScore("§7§l§c");
        //Score score3 = obj.getScore("§dEn cas de bug faites");
        //Score score2 = obj.getScore("§d/bug <votre bug> !");
        Score score1 = obj.getScore("§c§b§l---------------§r");
        Score score0 = obj.getScore("§b§lmc.deadpvp.com");

        score0.setScore(0);
        score1.setScore(1);
        //score2.setScore(2);
        //score3.setScore(3);
        score6.setScore(6);

        score3.setScore(3);
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
        grade.setSuffix(ChatUtils.getPrefix(player)+"");
        obj.getScore(ChatColor.GOLD + "" + ChatColor.LIGHT_PURPLE).setScore(13);

        Team onlineCounter = board.registerNewTeam("onlineCounter");
        onlineCounter.addEntry(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC);
        onlineCounter.setPrefix(ChatColor.WHITE+"≫ Créatif: ");
        onlineCounter.setSuffix("§6"+Bukkit.getOnlinePlayers().size()+"/§c"+ Main.getInstance().playerCount);
        obj.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC).setScore(9);




        player.setScoreboard(board);
    }
    public static void updateScoreBoard(Player player){
        Scoreboard board = player.getScoreboard();
        try {
            Object karmaint = sqlUtilities.getData("moneyserv","player",player.getName(),"karma","Int");
            board.getTeam("Karma").setSuffix("§d"+karmaint.toString());
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
        board.getTeam("onlineCounter").setSuffix("§6"+Bukkit.getOnlinePlayers().size()+"/§c"+Main.getInstance().playerCount);
    }
}
