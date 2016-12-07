package com.hemmingfield.stables.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage
        implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if ((entity instanceof Horse)) {
            Horse horse = (Horse) entity;
            if (horse.hasMetadata("inside_stable")) {
                event.setCancelled(true);
            }
        }
        if ((entity.hasMetadata("merchant")) &&
                (entity.getMetadata("merchant").get(0).asBoolean())) {
            event.setCancelled(true);
        }
    }
}



