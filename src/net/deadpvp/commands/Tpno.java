package net.deadpvp.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpno implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            Player tphere = Tpahere.tpahere.get(p);
            if(Tpahere.tpahere.containsKey(p)){
                tphere.sendMessage("§5§lTéléportation>>> §cTéléportation refusée !");
                p.sendMessage("§5§lTéléportation>>> §cTéléportation refusée !");
                tphere.playSound(tphere.getLocation(), Sound.ENTITY_VILLAGER_NO,10,1);
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO,10,2);
                Tpahere.tpahere.remove(p);
                return true;

            }
            if(Tpa.tpa.containsKey(p)){

                tphere.sendMessage("§5§lTéléportation>>> §cTéléportation refusée !");
                p.sendMessage("§5§lTéléportation>>> §cTéléportation refusée !");
                tphere.playSound(tphere.getLocation(), Sound.ENTITY_VILLAGER_NO,10,1);
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO,10,2);
                Tpa.tpa.remove(p);
                return true;
            }else{
                p.sendMessage("§cErreur: vous n'avez pas de demande de téléportation en attente !");
                return false;

            }
        }
        return false;
    }
}
