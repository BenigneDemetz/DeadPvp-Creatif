package net.deadpvp.utils;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_16_R1.EnumHand;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.PacketPlayOutOpenBook;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class BookUtils {
    
    public static ItemStack createBook() {
        try {
            ItemStack book = new ItemStack (Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta) book.getItemMeta ();
            List<IChatBaseComponent> pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField ("pages").get (meta);
            
            //Modif les texte comme tu veut, si tu veut ajouter des chose, mettre dans le tableau ligne 35
            TextComponent text = new TextComponent ("         §4§l§nRég§1§l§nles\n\n");
            TextComponent regle1 = new TextComponent ("§0§l§nIl est interdit de:§r \n\n - Cheat \n - Insulter \n - Usebug \n - Spam \n - " +
                    "Grief \n - Construire des formes ou textes offensants ");
            
            //quand il clique pour accepter ça trigger une cmd et ajoute le mec dans une arraylist, c'est pas ouf comme méthode mais nique ça mardhe
            //et c'est 2h du mat
            TextComponent accept = new TextComponent ("\n\n§a§l  Cliquez ici pour \n      accepter.");
            accept.setClickEvent (new ClickEvent (ClickEvent.Action.RUN_COMMAND, "/dpaccept"));
            
            //Page 1, copier coller pour en ajouter une autre, pour le texte, fait d'autre textcomponent et tu les met dans le tableau
            IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a (ComponentSerializer.toString (new BaseComponent[]{text, regle1, accept}));
            pages.add (page);
            
            meta.setTitle ("Régles");
            meta.setAuthor ("DeadPvP");
            book.setItemMeta (meta);
            return book;
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex){
            ex.printStackTrace ();
        }
    
        return null;
    }
    //pas touche a ça
    public static void openBook(final ItemStack book, final Player player) {
        final int slot = player.getInventory().getHeldItemSlot();
        final ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, book);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutOpenBook (EnumHand.MAIN_HAND));
        player.getInventory().setItem(slot, old);
    }



}
