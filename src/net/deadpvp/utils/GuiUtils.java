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

        ItemBuilder tpplot = new ItemBuilder(Material.OAK_DOOR).setName("§dTp à ton Plot");

        inventory.setItem(1*9+2, tpplot.toItemStack());

        inventory = gamemodes(p,inventory);


        return inventory;
    }

    private static Inventory gamemodes(Player p, Inventory inventory) {
        GameMode gameMode = p.getGameMode();
        ItemBuilder aventure = new ItemBuilder(Material.IRON_SWORD).setName("§dMode Aventure");
        ItemStack aventureItemStack = aventure.toItemStack();
        if (gameMode.equals(GameMode.ADVENTURE))
            aventureItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta itemMetaAventure = aventureItemStack.getItemMeta();
        itemMetaAventure.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMetaAventure.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        ItemBuilder spec = new ItemBuilder(Material.GLASS).setName("§dMode Spectateur");
        ItemStack specItemStack = spec.toItemStack();
        if (gameMode.equals(GameMode.SPECTATOR))
            specItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta itemMetaSpec = specItemStack.getItemMeta();
        itemMetaSpec.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        ItemBuilder survie = new ItemBuilder(Material.STONE_PICKAXE).setName("§dMode Survie");
        ItemStack survieItemStack = survie.toItemStack();
        if (gameMode.equals(GameMode.SURVIVAL))
            survieItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemMeta itemMetaSurvie = survieItemStack.getItemMeta();
        itemMetaSurvie.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMetaSurvie.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ItemBuilder crea = new ItemBuilder(Material.CRAFTING_TABLE).setName("§dMode Créatif");
        ItemStack itemStackCrea = crea.toItemStack();
        if (gameMode.equals(GameMode.CREATIVE))
            itemStackCrea.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta imcrea = crea.toItemStack().getItemMeta();
        imcrea.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemStackCrea.setItemMeta(imcrea);
        aventureItemStack.setItemMeta(itemMetaAventure);
        specItemStack.setItemMeta(itemMetaSpec);
        survieItemStack.setItemMeta(itemMetaSurvie);

        inventory.setItem(3*9+5, aventureItemStack);
        inventory.setItem(3*9+6, specItemStack);
        inventory.setItem(3*9+7, survieItemStack);
        inventory.setItem(3*9+8, itemStackCrea);
        return inventory;
    }



}
