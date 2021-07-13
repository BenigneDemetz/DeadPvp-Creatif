package net.deadpvp.gui;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;

public abstract class GuiManager implements InventoryHolder {

    protected Inventory inv;
    protected PlayerGuiUtils playerGuiUtils;

    public GuiManager(PlayerGuiUtils playerGuiUtils) {
        this.playerGuiUtils = playerGuiUtils;
    }


    /*
    * Comment fonctionne cette classe :
    *  Cette classe est une classe abstraite: CAD qu'elle n'est pas instanciable;
    * elle sert a etre etendu dans une autre classe afin de recuperer chacune des methode abstraite ci-dessous
    *
    * pour crée un nouveau Gui, faire une nouvelle classe dans net.deadpvp.gui.guis (ne marcheras pas autre part puisque inv = protected)
    *
    * extend cette class avec GuiManager, importer les methode et crée le constructeur (
    *
    *           public NomDeLaClasse(PlayerGuiUtils playerGuiUtils){
    *               super(playerGuiUtils);     //super sert a se renvoyer a la classe etendu; ici GuiManager
    *           }
    * )
    *
    * pour getSlots et getName, simplement return le nombre de slots sur getSlots et le nom sur getName
    *
    * Mettre les event du gui dans EventHandler();
    *
    * Mettre les Item du gui dans setItems; ne pas oublier de inv.setItem chacun des item dans le setItem (comme vous extendez la classe, pas besoin d'initialier un variable inv comme
    * elle l'est deja ici;
    *
    * Ensuite, pour ouvrir l'inventaire, simplement crée l'iventaire en mettant le playerGuiUtils en parametre
    * Exemple : pour ouvrir CommamndGui :
    *
    *       CommandGui commandGui = new CommandGui(Main.getPlayerGuiUtils(player))
    *       commandGui.openInv();
    */
    public abstract int getSlots();
    public abstract String getName();
    public abstract void EventHandler(InventoryClickEvent e);
    public abstract void setItems();

    public void openInv(){
        inv = Bukkit.createInventory(this, getSlots(), getName());

        this.setItems();

        playerGuiUtils.getPlayer().openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

}
