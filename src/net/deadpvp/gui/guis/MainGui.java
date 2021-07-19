package net.deadpvp.gui.guis;

import com.yapzhenyie.GadgetsMenu.GadgetsMenu;
import net.deadpvp.Main;
import net.deadpvp.gui.GuiManager;
import net.deadpvp.gui.PlayerGuiUtils;
import net.deadpvp.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainGui extends GuiManager {

    public MainGui(PlayerGuiUtils playerGuiUtils) {
        super(playerGuiUtils);
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public String getName() {
        return "§bCommandes";
    }

    @Override
    public void EventHandler(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        MainGui mainGui = new MainGui(Main.getPlayerGuiUtils(p));
        switch (e.getCurrentItem().getType()){

            case OAK_DOOR:
                p.closeInventory();
                p.performCommand("plot home");
                break;
            case NETHER_STAR:
                p.performCommand("gadgetsmenu menu main");
                break;
            case GRASS_BLOCK:
                p.closeInventory();
                p.performCommand("spawn");
                break;
            case END_PORTAL_FRAME:
                p.performCommand("hub");
                break;
            case CRAFTING_TABLE:
                p.setGameMode(GameMode.CREATIVE);
                mainGui.openInv();
                break;
            case IRON_SWORD:
                p.setGameMode(GameMode.SURVIVAL);
                mainGui.openInv();
                break;
            case MAP:
                p.setGameMode(GameMode.ADVENTURE);
                mainGui.openInv();
                break;
            case ENDER_CHEST:
                GadgetsMenu.getPlayerManager(p).openMysteryVaultMenu(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void setItems() {
        ItemBuilder itemPlot = new ItemBuilder(Material.OAK_DOOR).setName("§d§lTp à ton Plot").addEnchant(Enchantment.DAMAGE_ALL, 1).hideAttributes();
        ItemBuilder vide = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("§d").hideAttributes();
        ItemBuilder cosmetique = new ItemBuilder(Material.NETHER_STAR).setName("§d§lCosmétique").hideAttributes();
        ItemBuilder spawn = new ItemBuilder(Material.GRASS_BLOCK).setName("§d§lSpawn").hideAttributes();
        ItemBuilder hub = new ItemBuilder(Material.END_PORTAL_FRAME).setName("§d§lHub").hideAttributes();
        ItemBuilder crea = new ItemBuilder(Material.CRAFTING_TABLE).setName("§d§lMode Créatif").hideAttributes();
        ItemBuilder survie = new ItemBuilder(Material.IRON_SWORD).setName("§d§lMode Survie").hideAttributes();
        ItemBuilder aventure = new ItemBuilder(Material.MAP).setName("§d§lMode Aventure").hideAttributes();
        ItemBuilder openVault = new ItemBuilder(Material.ENDER_CHEST).setName("§d§lOuvrir Mystery Boxes");
        ItemBuilder plotoptions = new ItemBuilder(Material.COMPARATOR).setName("§d§lOptions du plot");


        GameMode gameMode = playerGuiUtils.getPlayer().getGameMode();

        if(gameMode.equals(GameMode.CREATIVE)){
            crea.addEnchant(Enchantment.DAMAGE_ALL, 1);
        } else if (gameMode.equals(GameMode.SURVIVAL)) {
            survie.addEnchant(Enchantment.DAMAGE_ALL, 1);
        } else if (gameMode.equals(GameMode.ADVENTURE)){
            aventure.addEnchant(Enchantment.DAMAGE_ALL, 1);
        }

        inv.setItem(1*9+2,plotoptions.toItemStack()); //11
        inv.setItem(1*9+4, itemPlot.toItemStack()); // 13
        inv.setItem(1*9+6, cosmetique.toItemStack()); //15
        inv.setItem(3*9+4, openVault.toItemStack()); //16
        inv.setItem(3*9+0, hub.toItemStack()); // 27
        inv.setItem(3*9+1, spawn.toItemStack()); // 28
        inv.setItem(3*9+6, aventure.toItemStack()); // 33
        inv.setItem(3*9+7, survie.toItemStack()); // 34
        inv.setItem(3*9+8, crea.toItemStack()); // 35


        for(int x=0; x<inv.getSize();x++){
            if(inv.getItem(x) == null) inv.setItem(x,vide.toItemStack());
        }

    }
}
