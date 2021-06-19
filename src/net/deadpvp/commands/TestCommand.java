package net.deadpvp.commands;

import net.deadpvp.utils.BookUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            ItemStack book = BookUtils.createBook ();
            BookUtils.openBook (book, p);
        }
        return false;
    }
}
