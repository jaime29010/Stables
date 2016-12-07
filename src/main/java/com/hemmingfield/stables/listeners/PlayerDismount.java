package com.hemmingfield.stables.listeners;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerDismount
        implements Listener {
    @EventHandler
    public void onPlayerDismount(VehicleExitEvent event) {
        if (((event.getExited() instanceof Player)) &&
                ((event.getVehicle() instanceof Horse))) {
            Horse horse = (Horse) event.getVehicle();
            if (horse.hasMetadata("inside_stable")) {
                horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, -2147483648));
            }
        }
    }
}



