package com.hemmingfield.stables.command.commands;

import com.hemmingfield.stables.command.type.Game;
import com.hemmingfield.stables.managers.MerchantManager;
import com.hemmingfield.stables.managers.StableManager;
import com.hemmingfield.stables.util.Language;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMerchant
        extends Game {
    public String getName() {
        return "spawnmerchant";
    }

    public String getDescription() {
        return "Spawn a stable merchant at your location.";
    }

    public String getSyntax() {
        return "/caballos spawnmerchant <type> [name]";
    }

    public String getPermission() {
        return "caballos.spawnmerchant";
    }

    public void perform(Player player, String[] args) {
        if (args.length >= 1) {
            EntityType type = null;
            try {
                type = EntityType.valueOf(args[0]);
            } catch (Exception e) {
                Language.ERROR_INVALID_ENTITY_TYPE.sendTo(player);
            }
            Location location = player.getLocation();
            if (!StableManager.getInstance().intersects(location.getBlock())) {
                Language.ERROR_OUTSIDE_STABLE.sendTo(player);
                return;
            }
            String name = null;
            if (args.length == 2) {
                name = ChatColor.translateAlternateColorCodes('&', args[1]);
            }
            MerchantManager.getInstance().spawnMerchant(type, location, name);
            Language.MERCHANT_SPAWNED.sendTo(player);
        } else {
            Language.ERROR_INCORRECT_SYNTAX.sendTo(player, "%syntax%", getSyntax());
        }
    }
}



