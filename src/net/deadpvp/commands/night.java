package net.deadpvp.commands;

import net.minecraft.server.v1_16_R1.ItemFireworks;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class night implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command,String s, String[] args){
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            if(!p.hasPermission("chat.architecte")){
                p.sendMessage("§cErreur: vous n'avez pas les permissions nécessaires !");
                return true;
            }
            for(PotionEffect potion: p.getActivePotionEffects()){
                if(potion.getType().getName() == "NIGHT_VISION"){
                    p.sendMessage("§cNight vision désactivé !");
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);

                    return true;
                }

            }
            p.sendMessage("§aNight vision activé !");
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 100,false,false));
            return true;


        }
        return false;
    }
}
