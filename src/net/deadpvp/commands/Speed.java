package net.deadpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Speed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("deadpvp.creative.speed")) {
                int walkSpeed = (int) (p.getWalkSpeed()*10);
                int flySpeed = (int) (p.getFlySpeed()*10);
                if (args.length >= 1) {
                    try {
                        if (args[0].contains("reset")) {
                            p.setFlySpeed((float) 0.1);
                            p.setWalkSpeed((float) 0.2);
                            p.sendMessage("§eVitesse reset");
                            return true;
                        }
                        if (Float.parseFloat(args[0]) > 10) return false;

                        else if (p.hasPermission("deadpvp.architecte")) ;

                        else if (p.hasPermission("deadpvp.constructeur")) {
                            if (Float.parseFloat(args[0]) > 7) {
                                p.sendMessage("§cTu ne peux pas dépasser la vitesse de 7");
                                return true;
                            }
                        } else if (p.hasPermission("deadpvp.apprenti")) {
                            if (Float.parseFloat(args[0]) > 4) {
                                p.sendMessage("§cTu ne peux pas dépasser la vitesse de 4");
                                return true;
                            }
                        }
                        if (p.isFlying()) {
                            p.setFlySpeed(Float.parseFloat(args[0]) / 10);
                            p.sendMessage("§bSpeed en vol : §6 " + flySpeed + " -> " + args[0]);
                        } else if (p.isOnGround()) {
                            p.setWalkSpeed(Float.parseFloat(args[0]) / 10);
                            p.sendMessage("§bSpeed au sol : §6" + walkSpeed + " -> " + args[0]);
                        }
                    }
                    catch (NumberFormatException nb) {
                        return false;
                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                        return false;
                    }
                }
                else {
                    p.sendMessage("§bTu vol en vitesse : §6" + flySpeed +
                            " §7(1 par défault)\n§bTu marches en vitesse : §6" + walkSpeed + " §7(2 par défault)");
                    return true;
                }
            }
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        }
        else sender.sendMessage("Tu n'es pas un joueur");


        return true;
    }
}