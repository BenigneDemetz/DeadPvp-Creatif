package net.deadpvp.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class LivreBeta implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("§4§lDEAD§9§lPVP");
        meta.setTitle("§4BETA §dCarnet de commandes");
        book.setItemMeta(meta);

        ((Player) commandSender).getInventory().setItem(8, book);

        return false;
    }
}
