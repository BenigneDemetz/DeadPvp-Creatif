package net.deadpvp.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class GuiUtils {

    public static Inventory mainMenu (Player p){


        Inventory inventory = Bukkit.createInventory(null, 4*9, "§bCommandes");
        ItemBuilder vide = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("§d");
        for(int x=0; x<inventory.getSize();x++){
            inventory.setItem(x,vide.toItemStack());
        }

        ItemStack itemplot = new ItemStack(Material.OAK_DOOR);
        itemplot.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta meta = itemplot.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("§d§lTp à ton Plot");
        itemplot.setItemMeta(meta);
        inventory.setItem(1*9+2, itemplot);

        ItemBuilder cosmetique = new ItemBuilder(Material.NETHER_STAR).setName("§d§lCosmétiques");
        inventory.setItem(1*9+6, cosmetique.toItemStack());

        ItemBuilder openVault = new ItemBuilder(Material.ENDER_CHEST).setName("§d§lOuvrir Mystery Boxes");
        inventory.setItem(1*9+7, openVault.toItemStack());

        ItemBuilder spawn = new ItemBuilder(Material.GRASS_BLOCK).setName("§d§lSpawn");
        inventory.setItem(3*9+1, spawn.toItemStack());

        ItemBuilder hub = new ItemBuilder(Material.END_PORTAL_FRAME).setName("§d§lHub");
        inventory.setItem(3*9+0, hub.toItemStack());

        inventory = gamemodes(p,inventory);


        return inventory;
    }

    public static Inventory gamemodes(Player p, Inventory inventory) {
        GameMode gameMode = p.getGameMode();

        ItemStack aventureItemStack = advItem(false);
        if (gameMode.equals(GameMode.ADVENTURE))
            aventureItemStack = advItem(true);


        ItemStack specItemStack = specItem(false);
        if (gameMode.equals(GameMode.SPECTATOR))
            specItemStack = advItem(true);


        ItemStack survieItemStack = survieItem(false);
        if (gameMode.equals(GameMode.SURVIVAL))
            survieItemStack = survieItem(true);

        ItemStack itemStackCrea = creaItem(false);
        if (gameMode.equals(GameMode.CREATIVE))
             itemStackCrea = creaItem(true);

        if (p.hasPermission("chat.builder")) inventory.setItem(3*9+4, adminItem());


        inventory.setItem(3*9+8, aventureItemStack);
        //inventory.setItem(3*9+6, specItemStack);
        inventory.setItem(3*9+7, survieItemStack);
        inventory.setItem(3*9+6, itemStackCrea);
        return inventory;
    }

    public static ItemStack creaItem (boolean enchant) {
        ItemBuilder crea = new ItemBuilder(Material.CRAFTING_TABLE).setName("§d§lMode Créatif");
        ItemStack itemStackCrea = crea.toItemStack();
        if (enchant)
            itemStackCrea.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta imcrea = crea.toItemStack().getItemMeta();
        imcrea.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStackCrea.setItemMeta(imcrea);
        return itemStackCrea;
    }

    public static ItemStack survieItem (boolean enchant) {
        ItemBuilder survie = new ItemBuilder(Material.IRON_SWORD).setName("§d§lMode Survie");
        ItemStack survieItemStack = survie.toItemStack();
        if (enchant)
            survieItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemMeta itemMetaSurvie = survieItemStack.getItemMeta();
        itemMetaSurvie.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMetaSurvie.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        survieItemStack.setItemMeta(itemMetaSurvie);
        return survieItemStack;
    }

    public static ItemStack advItem (boolean enchant) {
        ItemBuilder aventure = new ItemBuilder(Material.MAP).setName("§d§lMode Aventure");
        ItemStack aventureItemStack = aventure.toItemStack();
        if (enchant)
            aventureItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta itemMetaAventure = aventureItemStack.getItemMeta();
        itemMetaAventure.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMetaAventure.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        aventureItemStack.setItemMeta(itemMetaAventure);
        return aventureItemStack;
    }

    public static ItemStack specItem (boolean enchant) {
        ItemBuilder spec = new ItemBuilder(Material.ENDER_EYE).setName("§d§lMode Spectateur");
        ItemStack specItemStack = spec.toItemStack();
        if (enchant)
            specItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta itemMetaSpec = specItemStack.getItemMeta();
        itemMetaSpec.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        specItemStack.setItemMeta(itemMetaSpec);
        return specItemStack;
    }

    public static ItemStack adminItem () {
        ItemBuilder admin = new ItemBuilder(Material.COMMAND_BLOCK).setName("§d§lMenu Administrateur");
        ItemStack adminItemStack = admin.toItemStack();
        adminItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta itemMetaSpec = adminItemStack.getItemMeta();
        itemMetaSpec.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        adminItemStack.setItemMeta(itemMetaSpec);
        return adminItemStack;
    }

}
