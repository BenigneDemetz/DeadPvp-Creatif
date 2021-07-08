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

        ItemBuilder tpplot = new ItemBuilder(Material.OAK_DOOR).setName("§dTp à ton Plot");
        inventory.setItem(1*9+2, tpplot.toItemStack());

        ItemBuilder cosmetique = new ItemBuilder(Material.NETHER_STAR).setName("§d§lCosmétique");
        inventory.setItem(1*9+6, cosmetique.toItemStack());



        inventory = gamemodes(p,inventory);


        return inventory;
    }

    private static Inventory gamemodes(Player p, Inventory inventory) {
        GameMode gameMode = p.getGameMode();
        ItemBuilder aventure = new ItemBuilder(Material.MAP).setName("§dMode Aventure");
        ItemStack aventureItemStack = aventure.toItemStack();
        if (gameMode.equals(GameMode.ADVENTURE))
            aventureItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta itemMetaAventure = aventureItemStack.getItemMeta();
        itemMetaAventure.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMetaAventure.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        ItemBuilder spec = new ItemBuilder(Material.ENDER_EYE).setName("§4§l[STAFF] §dMode Spectateur");
        ItemStack specItemStack = spec.toItemStack();
        if (gameMode.equals(GameMode.SPECTATOR))
            specItemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        ItemMeta itemMetaSpec = specItemStack.getItemMeta();
        itemMetaSpec.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        ItemBuilder survie = new ItemBuilder(Material.IRON_SWORD).setName("§dMode Survie");
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

        inventory.setItem(3*9+8, aventureItemStack);
        inventory.setItem(3*9+7, survieItemStack);
        inventory.setItem(3*9+6, itemStackCrea);
        if(p.hasPermission("chat.dev")){
            inventory.setItem(3*9+5, specItemStack);
        }
        return inventory;
    }



}
