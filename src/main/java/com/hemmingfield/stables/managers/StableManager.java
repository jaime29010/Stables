package com.hemmingfield.stables.managers;

import com.hemmingfield.stables.util.IdleHorse;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class StableManager {
    private static StableManager instance = new StableManager();
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private HashMap<UUID, Horse> active = new HashMap<UUID, Horse>();
    private HashMap<UUID, IdleHorse> horses = new HashMap<UUID, IdleHorse>();

    public static StableManager getInstance() {
        return instance;
    }

    public Horse getHorse(Player player) {
        if (this.active.containsKey(player.getUniqueId())) {
            return this.active.get(player.getUniqueId());
        }
        return null;
    }

    public IdleHorse getIdleHorse(Player player) {
        if (this.horses.containsKey(player.getUniqueId())) {
            IdleHorse horse = this.horses.get(player.getUniqueId());
            this.horses.remove(player.getUniqueId());
            return horse;
        }
        return null;
    }

    public boolean intersects(Block block) {
        for (Block block1 : this.blocks) {
            if ((block1.getWorld() == block.getWorld()) &&
                    (block.getX() == block1.getX()) &&
                    (block.getY() == block1.getY()) &&
                    (block.getZ() == block1.getZ())) {
                return true;
            }
        }
        return false;
    }

    public void includeBlocks(Block[] blocks) {
        for (Block block : blocks) {
            if (!this.blocks.contains(block)) {
                this.blocks.add(block);
            }
        }
    }

    public void storeHorse(Player player, IdleHorse horse) {
        this.horses.put(player.getUniqueId(), horse);
    }

    public void load(Plugin plugin) {
        File stableData = new File(plugin.getDataFolder(), "stable.yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(stableData);
        if (!stableData.exists()) {
            plugin.getLogger().info("Error: no stable data to load.");
            return;
        }
        for (int i = 0; data.isSet("block." + i); i++) {
            this.blocks.add(((Location) data.get("block." + i)).getBlock());
        }
        if (data.isSet("horse")) {
            for (String owner : data.getConfigurationSection("horse").getKeys(false)) {
                double level = data.getDouble("horse." + owner + ".speed");
                double jump = data.getDouble("horse." + owner + ".jump");
                int breed = data.getInt("horse." + owner + ".breed");
                Horse.Style style = Horse.Style.valueOf(data.getString("horse." + owner + ".style"));
                Horse.Color color = Horse.Color.valueOf(data.getString("horse." + owner + ".color"));
                Horse.Variant variant = Horse.Variant.valueOf(data.getString("horse." + owner + ".variant"));
                ItemStack armor = data.getItemStack("horse." + owner + ".armor");
                String name = data.getString("horse." + owner + ".name");
                boolean isNameVisible = data.getBoolean("horse." + owner + ".isNameVisibile");
                boolean isOnFire = data.getBoolean("horse." + owner + ".isOnFire");
                int nauseaTime = data.getInt("horse." + owner + ".nauseaTime");
                boolean isVip = data.getBoolean("horse." + owner + ".isVip");
                int blindnessTime = data.getInt("horse." + owner + ".blindnessTime");
                boolean flies = data.getBoolean("horse." + owner + ".flies");
                int speedTime = data.getInt("horse." + owner + ".speedTime");
                this.horses.put(UUID.fromString(owner), new IdleHorse(level, jump, breed, style, color, variant, armor, name, isNameVisible, isOnFire, nauseaTime, blindnessTime, isVip, flies, speedTime));
            }
        }
        plugin.getLogger().info("Success: stable data loaded.");
    }

    public void loadHorse(Player player, Horse horse) {
        this.active.put(player.getUniqueId(), horse);
    }

    public void save(Plugin plugin) {
        File stableData = new File(plugin.getDataFolder(), "stable.yml");
        if (stableData.exists()) {
            stableData.delete();
        }
        YamlConfiguration data = YamlConfiguration.loadConfiguration(stableData);

        int i = 0;
        for (Block block : this.blocks) {
            data.set("block." + i, block.getLocation());
            i++;
        }
        for (UUID owner : this.active.keySet()) {
            Horse horse = this.active.get(owner);
            double speed = 1.0D;
            boolean isOnFire = false;
            int nauseaTime = 0;
            int blindnessTime = 0;
            boolean isVip = false;
            boolean flies = false;
            int speedTime = 0;
            if (horse.hasMetadata("speed")) {
                speed = horse.getMetadata("speed").get(0).asDouble();
            }
            if (horse.hasMetadata("isOnFire")) {
                isOnFire = horse.getMetadata("isOnFire").get(0).asBoolean();
            }
            if (horse.hasMetadata("nauseaTime")) {
                nauseaTime = horse.getMetadata("nauseaTime").get(0).asInt();
            }
            if (horse.hasMetadata("blindnessTime")) {
                blindnessTime = horse.getMetadata("blindnessTime").get(0).asInt();
            }
            if (horse.hasMetadata("isVip")) {
                isVip = horse.getMetadata("isVip").get(0).asBoolean();
            }
            if (horse.hasMetadata("flies")) {
                flies = horse.getMetadata("flies").get(0).asBoolean();
            }
            if (horse.hasMetadata("speedTime")) {
                speedTime = horse.getMetadata("speedTime").get(0).asInt();
            }

            data.set("horse." + owner.toString() + ".speed", Double.valueOf(speed));
            data.set("horse." + owner.toString() + ".jump", Double.valueOf(horse.getJumpStrength()));
            data.set("horse." + owner.toString() + ".style", horse.getStyle().toString());
            data.set("horse." + owner.toString() + ".color", horse.getColor().toString());
            data.set("horse." + owner.toString() + ".variant", horse.getVariant().toString());
            data.set("horse." + owner.toString() + ".armor", horse.getInventory().getArmor());
            data.set("horse." + owner.toString() + ".name", horse.getCustomName());
            data.set("horse." + owner.toString() + ".isNameVisibile", horse.isCustomNameVisible());
            data.set("horse." + owner.toString() + ".isOnFire", isOnFire);
            data.set("horse." + owner.toString() + ".nauseaTime", nauseaTime);
            data.set("horse." + owner.toString() + ".blindnessTime", blindnessTime);
            data.set("horse." + owner.toString() + ".isVip", isVip);
            data.set("horse." + owner.toString() + ".flies", flies);
            data.set("horse." + owner.toString() + ".speedTime", speedTime);
            horse.remove();
        }
        for (UUID owner : this.horses.keySet()) {
            IdleHorse horse = this.horses.get(owner);
            data.set("horse." + owner.toString() + ".speed", Double.valueOf(horse.speed));
            data.set("horse." + owner.toString() + ".jump", Double.valueOf(horse.jump));
            data.set("horse." + owner.toString() + ".style", horse.style.toString());
            data.set("horse." + owner.toString() + ".color", horse.color.toString());
            data.set("horse." + owner.toString() + ".variant", horse.variant.toString());
            data.set("horse." + owner.toString() + ".armor", horse.armor);
            data.set("horse." + owner.toString() + ".name", horse.name);
            data.set("horse." + owner.toString() + ".isNameVisibile", horse.isNameVisible);
            data.set("horse." + owner.toString() + ".isOnFire", horse.isOnFire);
            data.set("horse." + owner.toString() + ".nauseaTime", horse.nauseaTime);
            data.set("horse." + owner.toString() + ".blindnessTime", horse.blindnessTime);
            data.set("horse." + owner.toString() + ".isVip", horse.isVip);
            data.set("horse." + owner.toString() + ".flies", horse.flies);
            data.set("horse." + owner.toString() + ".speedTime", horse.speedTime);
        }
        try {
            data.save(stableData);
            plugin.getLogger().info("Success: stable data saved.");
        } catch (IOException e) {
            plugin.getLogger().info("Error: unable to save stable data.");
        }
    }

    public void unloadHorse(Player player) {
        this.active.remove(player.getUniqueId());
    }
}



