package com.hemmingfield.stables.listeners;

import com.hemmingfield.stables.util.Language;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffectType;

public class HorseMount
        implements Listener {
    @EventHandler
    public void onHorseMount(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player)) {
            return;
        }
        if (!(event.getVehicle() instanceof Horse)) {
            return;
        }
        Player player = (Player) event.getEntered();
        Horse horse = (Horse) event.getVehicle();
        if ((horse.hasMetadata("owner")) &&
                (!horse.getMetadata("owner").get(0).asString().equals(player.getUniqueId().toString()))) {
            event.setCancelled(true);
            Language.HORSE_NOT_OWN.sendTo(player);
        } else if ((horse.hasMetadata("owner")) &&
                (horse.getMetadata("owner").get(0).asString().equals(player.getUniqueId().toString()))) {
            horse.removePotionEffect(PotionEffectType.SPEED);
        }
    }
}



