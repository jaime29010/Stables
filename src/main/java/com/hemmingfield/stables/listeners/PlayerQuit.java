package com.hemmingfield.stables.listeners;

import com.hemmingfield.stables.managers.StableManager;
import com.hemmingfield.stables.util.IdleHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerQuit
        implements Listener {
    private ArrayList<UUID> reimburse = new ArrayList<UUID>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Horse horse = StableManager.getInstance().getHorse(player);
        if (horse == null) {
            return;
        }
        StableManager.getInstance().storeHorse(player, new IdleHorse(horse));

        this.reimburse.add(player.getUniqueId());
    }
}



