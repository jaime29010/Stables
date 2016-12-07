package com.hemmingfield.stables.command.commands;

import com.hemmingfield.stables.command.type.Game;
import com.hemmingfield.stables.listeners.SelectionHandler;
import com.hemmingfield.stables.managers.StableManager;
import com.hemmingfield.stables.util.Language;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CreateStable
        extends Game {
    public String getName() {
        return "createstable";
    }

    public String getDescription() {
        return "Create a stable using the selected region.";
    }

    public String getSyntax() {
        return "/caballos createstable";
    }

    public String getPermission() {
        return "caballos.createstable";
    }

    public void perform(Player player, String[] args) {
        Location max = SelectionHandler.getInstance().getMax(player);
        Location min = SelectionHandler.getInstance().getMin(player);
        if ((max == null) ||
                (min == null)) {
            Language.ERROR_NO_SELECTION.sendTo(player);
            return;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (double x = min.getX(); x <= max.getX(); x += 1.0D) {
            for (double y = min.getY(); y <= max.getY(); y += 1.0D) {
                for (double z = min.getZ(); z <= max.getZ(); z += 1.0D) {
                    blocks.add(new Location(max.getWorld(), x, y, z).getBlock());
                }
            }
        }
        StableManager.getInstance().includeBlocks(blocks.toArray(new Block[blocks.size()]));

        Language.STABLE_CREATED.sendTo(player);
    }
}



