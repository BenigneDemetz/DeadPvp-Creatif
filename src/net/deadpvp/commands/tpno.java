package net.deadpvp.commands;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpno implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            if(tpa.tpa.containsKey(p)){
                Player tphere = tpa.tpa.get(p);
                tphere.sendMessage("§5§lTéléportation>>> §cTéléportation refusée !");
                commandSender.sendMessage("§5§lTéléportation>>> §cTéléportation refusée !");
                tphere.playSound(tphere.getLocation(), Sound.ENTITY_VILLAGER_NO,10,1);
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO,10,2);
                tpa.tpa.remove(p);
                return true;
            }else{
                p.sendMessage("§cErreur: vous n'avez pas de demande de téléportation en attente !");
                return false;

            }
        }
        return false;
    }
}
