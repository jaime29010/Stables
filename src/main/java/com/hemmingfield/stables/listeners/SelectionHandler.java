package com.hemmingfield.stables.listeners;

import com.hemmingfield.stables.util.Language;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class SelectionHandler
        implements Listener {
    private static SelectionHandler instance = null;
    private HashMap<Player, Location> selectionOne = new HashMap<Player, Location>();
    private HashMap<Player, Location> selectionTwo = new HashMap<Player, Location>();

    public SelectionHandler() {
        instance = this;
    }

    public static SelectionHandler getInstance() {
        return instance;
    }

    @EventHandler
    public void onPlayerinteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if ((event.getClickedBlock() == null) ||
                (event.getClickedBlock().getType() == Material.AIR)) {
            return;
        }
        if ((player.isOp()) &&
                (player.getItemInHand().getType() == Material.DIAMOND_HOE)) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                this.selectionOne.put(player, event.getClickedBlock().getLocation());
                Language.SELECTION_ONE.sendTo(player);
                event.setCancelled(true);
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                this.selectionTwo.put(player, event.getClickedBlock().getLocation());
                Language.SELECTION_TWO.sendTo(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        this.selectionOne.remove(event.getPlayer());
        this.selectionTwo.remove(event.getPlayer());
    }

    public Location getMin(Player player) {
        if ((this.selectionOne.containsKey(player)) &&
                (this.selectionTwo.containsKey(player))) {
            Location one = this.selectionOne.get(player);
            Location two = this.selectionTwo.get(player);
            double x;
            double y;
            double z;
            if (one.getX() < two.getX()) {
                x = one.getX();
            } else {
                x = two.getX();
            }
            if (one.getY() < two.getY()) {
                y = one.getY();
            } else {
                y = two.getY();
            }
            if (one.getZ() < two.getZ()) {
                z = one.getZ();
            } else {
                z = two.getZ();
            }
            return new Location(one.getWorld(), x, y, z);
        }
        return null;
    }

    public Location getMax(Player player) {
        if ((this.selectionOne.containsKey(player)) &&
                (this.selectionTwo.containsKey(player))) {
            Location one = this.selectionOne.get(player);
            Location two = this.selectionTwo.get(player);
            double x;
            double y;
            double z;
            if (one.getX() > two.getX()) {
                x = one.getX();
            } else {
                x = two.getX();
            }
            if (one.getY() > two.getY()) {
                y = one.getY();
            } else {
                y = two.getY();
            }
            if (one.getZ() > two.getZ()) {
                z = one.getZ();
            } else {
                z = two.getZ();
            }
            return new Location(one.getWorld(), x, y, z);
        }
        return null;
    }
}



