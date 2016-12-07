package com.hemmingfield.stables.listeners;

import com.hemmingfield.stables.gui.PurchaseMenu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntity
        implements Listener {
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if ((entity.hasMetadata("merchant")) &&
                (entity.getMetadata("merchant").get(0).asBoolean())) {
            event.setCancelled(true);
            PurchaseMenu.getInstance().display(player);
        }
    }
}



