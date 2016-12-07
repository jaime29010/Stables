package com.hemmingfield.stables.listeners;

import com.hemmingfield.stables.Stables;
import com.hemmingfield.stables.managers.StableManager;
import com.hemmingfield.stables.util.IdleHorse;
import com.hemmingfield.stables.util.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class PlayerMove
        implements Listener {
    private ArrayList<Player> stableSession = new ArrayList<>();
    private ArrayList<Player> blockedPlayers = new ArrayList<>();

    private void blockPlayer(final Player p) {
        blockedPlayers.add(p);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Stables.getPlugin(), () -> {
            if (blockedPlayers.contains(p)) {
                blockedPlayers.remove(p);
            }
        }, 20L * 10);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        boolean insideStable = StableManager.getInstance().intersects(player.getLocation().getBlock());
        if (StableManager.getInstance().getHorse(player) != null) {
            Horse horse = StableManager.getInstance().getHorse(player);
            if (player.getLocation().getWorld() != horse.getLocation().getWorld() || player.getLocation().distance(horse.getLocation()) >= 75.0D) {
                StableManager.getInstance().storeHorse(player, new IdleHorse(horse));
                StableManager.getInstance().unloadHorse(player);
                Language.HORSE_INACTIVE_STORED.sendTo(player);
            }
        }
        if ((!insideStable) &&
                (this.stableSession.contains(player))) {
            this.stableSession.remove(player);
        }
        if ((!insideStable) &&
                (player.getVehicle() != null) &&
                ((player.getVehicle() instanceof Horse))) {
            Horse horse = (Horse) player.getVehicle();
            horse.setMetadata("inside_stable", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), Boolean.valueOf(false)));
        }
        if ((insideStable) &&
                (player.getVehicle() != null) &&
                ((player.getVehicle() instanceof Horse)) && !blockedPlayers.contains(player)) {
            blockPlayer(player);
            Horse horse = (Horse) player.getVehicle();
            if ((horse.hasMetadata("inside_stable")) &&
                    ((horse.getMetadata("inside_stable").get(0)).asBoolean())) {
                return;
            }
            StableManager.getInstance().storeHorse(player, new IdleHorse(horse));
            this.stableSession.add(player);
            StableManager.getInstance().unloadHorse(player);
            Language.HORSE_STORED.sendTo(player);
        } else if ((insideStable) &&
                (!this.stableSession.contains(player)) &&
                (player.getVehicle() == null) && !blockedPlayers.contains(player)) {
            blockPlayer(player);
            IdleHorse horse = StableManager.getInstance().getIdleHorse(player);
            if (horse == null) {
                return;
            }
            horse.spawn(player);
        }
    }
}



