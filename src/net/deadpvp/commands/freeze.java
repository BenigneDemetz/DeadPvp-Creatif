package net.deadpvp.commands;

import net.deadpvp.Main;
import net.deadpvp.events.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class freeze implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            if(p.hasPermission("chat.builder")){
                Player Freeze = Bukkit.getPlayer(args[0]);
                if(Freeze == null){
                    p.sendMessage("§cErreur: joueur introuvable !");
                    return true;
                }else{
                    if(!Freeze.hasPermission("chat.builder")){
                        if(Main.freeze.contains(Freeze)){
                            Main.freeze.remove(Freeze);
                            Freeze.sendTitle("§f ","§f ");
                        }else{
                            Main.freeze.add(Freeze);
                            Freeze.sendTitle("§c§lVous êtes freeze !","§6§lMerci de venir sur discord: discord.gg/23kPxkbzDg");
                            Freeze.sendMessage("§c§lVous §c§lvenez §c§ld'être §c§lfreeze par §c§lun§c§l membre§c§l §c§ldu §c§lstaff ! §c§lMerci §c§lde §c§lvous §c§lrendre §c§lsur §c§lle §c§ldiscord §c§lde §c§lDEADPVP, §c§ldiscord.gg/23kPxkbzDg §c§l,sans §c§lvous §c§ldéconnectez §c§l!");
                        }
                    }return false;

                }
            }
        }
        return false;
    }
}
