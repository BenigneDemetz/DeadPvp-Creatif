package net.deadpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class tpa implements CommandExecutor {
    public static Map<Player, Player> tpa = new HashMap<Player, Player>();
    Map<Player, Long> cooldowns2 = new HashMap<Player, Long>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command,String s, String[] args) {
        if(commandSender instanceof Player){
            if(args.length == 0){
                commandSender.sendMessage("§cErreur: /tpa <Player>");
                return false;
            }
            Player tpto = Bukkit.getPlayer(args[0]);
            Player p = (Player) commandSender;

            if(cooldowns2.containsKey(p)) {
                if(cooldowns2.get(p) > System.currentTimeMillis()) {
                    long timeleft=((cooldowns2.get(p)- System.currentTimeMillis())/1000)+1;
                    p.sendMessage("§cErreur: vous devez attendre "+timeleft+" secondes !");
                    return true;
                }

            }

            cooldowns2.put(p, System.currentTimeMillis()+(5*1000));


            if(tpto == null){
                p.sendMessage("§cErreur: joueur introuvable !");
                return false;
            }
            if(tpto.getName() == p.getName()){
                p.sendMessage("§cErreur: impossible de te téléporter à toi même !");
                return false;
            }
            tpa.put(tpto,p);
            p.sendMessage("§5§lTéléportation>>> §6Demande §6de §6téléportation §6à §b"+tpto.getName()+" §6envoyé§6!");
            tpto.sendMessage("§5§lTéléportation>>> §b"+p.getName()+" §6veut §6se §6téléporter §6à §6vous ! \n§5§lTéléportation>>> §6Faites §6/tpyes §6pour §6accepter §6et §6/tpno §6pour §6refuser §6!");

        }
        return false;
    }
}
