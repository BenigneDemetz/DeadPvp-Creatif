package net.deadpvp.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.deadpvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class hub implements CommandExecutor {
    
    private Main main;
    
    public hub(Main main) {
        this.main = main;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
       if(!(sender instanceof Player)) return true;
       
        Player p = (Player) sender;
        
        ByteArrayDataOutput doss = ByteStreams.newDataOutput();
        doss.writeUTF("Connect");
        doss.writeUTF("lobby"); // Le nom du srv
        Player player = Bukkit.getPlayerExact(p.getName());
        player.sendPluginMessage(main, "BungeeCord", doss.toByteArray());
        
        return false;
    }
}
