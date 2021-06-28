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
            if (p.hasPermission("deadpvp.speed")) {
                int walkSpeed = (int) (p.getWalkSpeed()*10);
                int flySpeed = (int) (p.getFlySpeed()*10);
                if (args.length >= 1) {
                    try {
                        if (Float.parseFloat(args[0]) > 10) return false;


                        if (p.isFlying()) {
                            p.setFlySpeed(Float.parseFloat(args[0]) / 10);
                            p.sendMessage("Speed en vol : " + args[0]);
                        } else if (p.isOnGround()) {
                            p.setWalkSpeed(Float.parseFloat(args[0]) / 10);
                            p.sendMessage("Speed au sol : " + args[0]);
                        }
                    }
                    catch (NumberFormatException  e)
                    {
                        try {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (Float.parseFloat(args[1]) > 10) return false;


                            if (target.isFlying()) {
                                target.setFlySpeed(Float.parseFloat(args[1]) / 10);
                                target.sendMessage("Speed en vol : " + args[1]);
                            } else if (target.isOnGround()) {
                                target.setWalkSpeed(Float.parseFloat(args[1]) / 10);
                                target.sendMessage("Speed au sol : " + args[1]);
                            }
                        }
                        catch (Exception ee)
                        {
                            return false;
                        }
                    }
                    return true;
                }
                else {
                    p.sendMessage("Tu vol en vitesse : " + flySpeed +
                            "\nTu marches en vitesse : " + walkSpeed);
                    return true;
                }
            }
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        }
        else sender.sendMessage("Tu n'es pas un joueur");


        return false;
    }
}
