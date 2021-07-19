package net.deadpvp.commands;

import net.deadpvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class hat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if(p.hasPermission("chat.apprenti")){
                if(p.getInventory().getItemInMainHand().getType() == Material.AIR){
                    p.sendMessage("§cErreur: vous n'avez rien dans la main !");
                    return true;
                }
                if(p.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK){
                    p.sendMessage("§cErreur: impossible de faire cela avec cet item !");
                    return true;
                }

                if(p.getInventory().getHelmet() == null){
                    p.getInventory().setHelmet(p.getInventory().getItemInMainHand());
                    ItemBuilder air = new ItemBuilder(Material.AIR);
                    p.getInventory().setItemInMainHand(air.toItemStack());
                    p.sendMessage("§aVotre item vient d'être placé sur votre tête !");
                    return true;

                }else{
                    p.getInventory().addItem(p.getInventory().getHelmet().clone());
                    p.getInventory().setHelmet(p.getInventory().getItemInMainHand());
                    ItemBuilder air = new ItemBuilder(Material.AIR);
                    p.getInventory().setItemInMainHand(air.toItemStack());
                    p.sendMessage("§aVotre item vient d'être placé sur votre tête !");
                    return true;
                }

            }else{
                p.sendMessage("§cErreur: vous n'avez pas les permissiones nécessaires !");
            }
            return true;
        }else{
            return true;
        }
    }
}
