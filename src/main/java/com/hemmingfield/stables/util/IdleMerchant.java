package com.hemmingfield.stables.util;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class IdleMerchant {
    public EntityType type;
    public Location location;
    public String name;

    public IdleMerchant(EntityType type, Location location, String name) {
        this.type = type;
        this.location = location;
        this.name = name;
    }
}



