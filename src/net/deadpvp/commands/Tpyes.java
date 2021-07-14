package net.deadpvp.commands;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpyes implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            if(Tpa.tpa.containsKey(p)){
                Player tphere = Tpa.tpa.get(p);
                tphere.sendMessage("§5§lTéléportation>>> §2Téléportation en cours !");
                p.sendMessage("§5§lTéléportation>>> §2Téléportation acceptée !");
                tphere.teleport(p);
                tphere.playSound(tphere.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,10,2);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,10,2);
                p.getWorld().spawnParticle(Particle.PORTAL,p.getLocation(),10,40,p.getLocation().getX(),p.getLocation().getY()+1,p.getLocation().getZ());
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
