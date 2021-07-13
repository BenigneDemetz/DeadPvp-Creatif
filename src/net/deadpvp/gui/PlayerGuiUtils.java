package net.deadpvp.gui;

import org.bukkit.entity.Player;

public class PlayerGuiUtils {

    private Player owner;

    public PlayerGuiUtils(Player p) {
        this.owner = p;
    }

    public Player getPlayer() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
