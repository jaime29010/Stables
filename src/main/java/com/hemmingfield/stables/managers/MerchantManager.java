package com.hemmingfield.stables.managers;

import com.hemmingfield.stables.Stables;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class MerchantManager {
    private static MerchantManager instance = new MerchantManager();
    private ArrayList<org.bukkit.entity.Entity> merchants = new ArrayList<Entity>();
    private ArrayList<Chunk> chunks = new ArrayList<Chunk>();

    public static MerchantManager getInstance() {
        return instance;
    }

    public void load(Plugin plugin) {
        File merchantData = new File(plugin.getDataFolder(), "merchants.yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(merchantData);
        if (!merchantData.exists()) {
            return;
        }
        for (int i = 0; data.isSet(i + ".type"); i++) {
            EntityType type = EntityType.valueOf(data.getString(i + ".type"));
            Location location = (Location) data.get(i + ".location");
            String name = data.getString(i + ".name");
            spawnMerchant(type, location, name);
        }
        plugin.getLogger().info("Loaded all merchants on file.");
    }

    public void save(Plugin plugin) {
        File merchantData = new File(plugin.getDataFolder(), "merchants.yml");
        if (merchantData.exists()) {
            merchantData.delete();
        }
        YamlConfiguration data = YamlConfiguration.loadConfiguration(merchantData);

        int i = 0;
        for (org.bukkit.entity.Entity merchant : this.merchants) {
            data.set(i + ".type", merchant.getType().toString());
            data.set(i + ".location", merchant.getLocation());
            if (merchant.getCustomName() != null) {
                data.set(i + ".name", merchant.getCustomName());
            }
            merchant.remove();
            i++;
        }
        try {
            data.save(merchantData);
            plugin.getLogger().info("Saved merchants.");
        } catch (Exception e) {
            plugin.getLogger().info("Error: unable to save merchants.");
        }
    }

    public void spawnMerchant(EntityType type, Location location, String name) {
        Chunk chunk = location.getChunk();
        if (!chunk.isLoaded())
            chunk.load();

        org.bukkit.entity.Entity entity = location.getWorld().spawnEntity(location, type);
        if (type == EntityType.VILLAGER) {
            Villager villager = (Villager) entity;
            villager.setAdult();
            villager.setProfession(Villager.Profession.FARMER);
        } else if (type == EntityType.HORSE) {
            Horse horse = (Horse) entity;
            horse.setAdult();
            horse.setColor(Horse.Color.WHITE);
            horse.setDomestication(1);
        }
        if (name != null) {
            entity.setCustomName(name);
        }
        entity.setCustomNameVisible(true);
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        net.minecraft.server.v1_8_R3.NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);

        entity.setMetadata("merchant", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), Boolean.valueOf(true)));
        if (!chunks.contains(entity.getLocation().getChunk())) {
            chunks.add(entity.getLocation().getChunk());
        }
        this.merchants.add(entity);
    }

    public void removeMerchants() {
        for (Entity en : merchants) {
            en.remove();
        }
    }

    public boolean isVillagerChunk(Chunk c) {
        return chunks.contains(c);
    }
}



