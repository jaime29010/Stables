package com.hemmingfield.stables.listeners;

import com.hemmingfield.stables.gui.PurchaseMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose
        implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (PurchaseMenu.getInstance().isViewing(player)) {
            PurchaseMenu.getInstance().remove(player);
        }
    }
}



