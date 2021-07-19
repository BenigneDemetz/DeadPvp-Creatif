package net.deadpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Tpahere implements CommandExecutor {

    public static Map<Player, Player> tpa = new HashMap<Player, Player>();
    public static Map<Player, Player> tpahere = new HashMap<Player, Player>();

    Map<Player, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command,String s, String[] args) {
        if (!(commandSender instanceof Player)) return true;
        Player p = (Player) commandSender;
        if(!p.hasPermission("chat.apprenti")){
            commandSender.sendMessage("§cVous n'avez pas les permissions pour faire cela !");
            return true;
        }
        if (args.length == 0)  return false;
        Player target = Bukkit.getPlayer(args[0]);



        if (cooldowns.containsKey(p) && cooldowns.get(p) > System.currentTimeMillis()) {
            p.sendMessage("§cErreur: merci d'attendre quelque seconde avant de pouvoir faire cette commande a nouveau!");
            return true;
        }

        cooldowns.put(p, System.currentTimeMillis() + (5 * 1000));

        if (target == null) {
            p.sendMessage("§cErreur: joueur introuvable !");
            return true;
        }
        if (target.getName() == p.getName()) {
            p.sendMessage("§cErreur: impossible de te téléporter à toi même !");
            return true;
        }
        if(tpa.containsKey(p)){
            tpa.remove(p);
        }
        tpahere.put(target,p);
        p.sendMessage("§5§lTéléportation>>> §6Demande §6envoyé §6à §b" + target.getName());
        target.sendMessage("§5§lTéléportation>>> §b" + p.getName() + " §6veut §6vous §6téléporter §6à §6lui ! \n§5§lTéléportation>>> §6Faites §6/tpyes §6pour §6accepter §6et §6/tpno §6pour §6refuser §6!");
        return true;
    }
}
