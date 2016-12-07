package com.hemmingfield.stables.util;

import com.hemmingfield.stables.Stables;
import com.hemmingfield.stables.managers.StableManager;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class IdleHorse {
    public double speed = 0.8D;
    public double jump;
    public int breed;
    public Horse.Style style;
    public Horse.Color color;
    public ItemStack armor;
    public Horse.Variant variant;
    public String name;
    public boolean isNameVisible = false;
    public boolean isOnFire;
    public int nauseaTime;
    public int blindnessTime;
    public boolean isVip;
    public boolean flies;
    public int speedTime;
    private Plugin plugin = JavaPlugin.getPlugin(Stables.class);

    public IdleHorse(Horse horse) {
        if (horse.hasMetadata("speed")) {
            this.speed = horse.getMetadata("speed").get(0).asDouble();
        }
        this.jump = horse.getJumpStrength();

        this.breed = horse.getDomestication();

        this.style = horse.getStyle();

        this.color = horse.getColor();

        this.variant = horse.getVariant();

        this.armor = horse.getInventory().getArmor();

        this.name = horse.getCustomName();

        this.isNameVisible = horse.isCustomNameVisible();

        if (horse.hasMetadata("isOnFire")) {
            this.isOnFire = horse.getMetadata("isOnFire").get(0).asBoolean();
        }
        if (horse.hasMetadata("nauseaTime")) {
            this.nauseaTime = horse.getMetadata("nauseaTime").get(0).asInt();
        }
        if (horse.hasMetadata("blindnessTime")) {
            this.blindnessTime = horse.getMetadata("blindnessTime").get(0).asInt();
        }
        if (horse.hasMetadata("isVip")) {
            this.isVip = horse.getMetadata("isVip").get(0).asBoolean();
        }
        if (horse.hasMetadata("flies")) {
            this.flies = horse.getMetadata("flies").get(0).asBoolean();
        }
        if (horse.hasMetadata("speedTime")) {
            this.speedTime = horse.getMetadata("speedTime").get(0).asInt();
        }
        horse.getAge();

        horse.remove();
    }

    public IdleHorse(double speed, double jump, int breed, Horse.Style style, Horse.Color color, Variant variant, ItemStack armor, String name, boolean isCustomNameVisible, boolean isOnFire, int nauseaTime, int blindnessTime, boolean isVip, boolean flies, int speedTime) {
        this.speed = speed;
        this.jump = jump;
        this.breed = breed;
        this.style = style;
        this.color = color;
        this.variant = variant;
        this.armor = armor;
        this.name = name;
        this.isNameVisible = isCustomNameVisible;
        this.isOnFire = isOnFire;
        this.nauseaTime = nauseaTime;
        this.blindnessTime = blindnessTime;
        this.isVip = isVip;
        this.flies = flies;
        this.speedTime = speedTime;
    }

    public void spawn(final Player player) {
        @SuppressWarnings("deprecation")
        final Horse horse = (Horse) player.getWorld().spawnCreature(player.getLocation(), EntityType.HORSE);

        horse.setMetadata("speed", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), Double.valueOf(this.speed)));
        horse.setMetadata("isOnFire", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), this.isOnFire));
        horse.setMetadata("nauseaTime", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), this.nauseaTime));
        horse.setMetadata("blindnessTime", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), this.blindnessTime));
        horse.setMetadata("isVip", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), this.isVip));
        horse.setMetadata("speedTime", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), this.speedTime));
        horse.setMetadata("flies", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), this.flies));
        horse.setMetadata("inside_stable", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), Boolean.valueOf(true)));
        horse.setMetadata("owner", new FixedMetadataValue(JavaPlugin.getPlugin(Stables.class), player.getUniqueId().toString()));

        AttributeInstance speed = ((EntityLiving) ((CraftEntity) horse).getHandle())
                .getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        speed.setValue(this.speed);

        horse.setJumpStrength(0.8D);
        horse.setStyle(this.style);
        horse.setTamed(true);
        horse.setAdult();
        horse.setVariant(variant);
        horse.setColor(this.color);
        horse.setDomestication(this.breed);
        horse.setCustomName(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("horse-name").replace("%player%", player.getName())));
        if (isOnFire) {
            new BukkitRunnable() {
                public void run() {
                    if (horse.isDead()) {
                        cancel();
                    }
                    horse.setFireTicks(60);
                }
            }.runTaskTimer(plugin, 0, 20);
        }
        if (nauseaTime > 0) {
            new BukkitRunnable() {
                public void run() {
                    if (horse.isDead()) {
                        cancel();
                    }
                    for (Entity en : horse.getNearbyEntities(3, 3, 3)) {
                        if (en instanceof Player) {
                            if (en != player) {
                                Player p = (Player) en;
                                try {
                                    Resident r1 = TownyUniverse.getDataSource().getResident(player.getName());
                                    Resident r2 = TownyUniverse.getDataSource().getResident(p.getName());
                                    if (r1.getTown() != null && r2.getTown() != null && r1.getTown() != r2.getTown()) {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, nauseaTime, 1));
                                    }
                                } catch (NotRegisteredException e) {
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 30);
        }
        if (blindnessTime > 0) {
            new BukkitRunnable() {
                public void run() {
                    if (horse.isDead()) {
                        cancel();
                    }
                    for (Entity en : horse.getNearbyEntities(3, 3, 3)) {
                        if (en instanceof Player) {
                            if (en != player) {
                                Player p = (Player) en;
                                try {
                                    Resident r1 = TownyUniverse.getDataSource().getResident(player.getName());
                                    Resident r2 = TownyUniverse.getDataSource().getResident(p.getName());
                                    if (r1.getTown() != null && r2.getTown() != null && r1.getTown() != r2.getTown()) {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindnessTime, 1));
                                    }
                                } catch (NotRegisteredException e) {
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 30);
        }
        if (speedTime > 0) {
            new BukkitRunnable() {
                public void run() {
                    if (horse.isDead()) {
                        cancel();
                    }
                    for (Entity en : horse.getNearbyEntities(3, 3, 3)) {
                        if (en instanceof Player) {
                            if (en != player) {
                                Player p = (Player) en;
                                try {
                                    Resident r1 = TownyUniverse.getDataSource().getResident(player.getName());
                                    Resident r2 = TownyUniverse.getDataSource().getResident(p.getName());
                                    if (r1.getTown() != null && r2.getTown() != null && r1.getTown() == r2.getTown()) {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedTime, 1));
                                    }
                                } catch (NotRegisteredException e) {
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 30);
        }
        if (flies) {
            new BukkitRunnable() {
                public void run() {
                    if (horse.isDead()) {
                        cancel();
                    }
                    Location loc = horse.getLocation();
                    if (loc.getY() > 0) {
                        double y = 0.1;
                        if (loc.clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
                            horse.setVelocity(horse.getVelocity().setY(y));
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 0);
        }
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.getInventory().setArmor(this.armor);
        horse.setPassenger(player);
        if (StableManager.getInstance().getHorse(player) != null) {
            StableManager.getInstance().getHorse(player).remove();
        }
        StableManager.getInstance().loadHorse(player, horse);
        Language.HORSE_LOADED.sendTo(player, "%speed%", Double.toString(this.speed));
    }
}



