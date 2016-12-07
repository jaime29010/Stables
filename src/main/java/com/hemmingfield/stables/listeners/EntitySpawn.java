package com.hemmingfield.stables.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class EntitySpawn implements Listener {
    @EventHandler
    public void onHorseSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getType() == EntityType.HORSE && event.getSpawnReason() != SpawnReason.CUSTOM) {
            event.setCancelled(true);
        }
    }
}
