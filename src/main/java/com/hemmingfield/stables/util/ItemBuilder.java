package com.hemmingfield.stables.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class ItemBuilder {
    @SuppressWarnings("deprecation")
    public static ItemStack getItem(Plugin plugin, String path) {
        ItemStack item = null;
        if (plugin.getConfig().isSet(path + ".type")) {
            item = new ItemStack(Material.valueOf(plugin.getConfig().getString(path + ".type").toUpperCase()));
        } else if (plugin.getConfig().isSet(path + ".id")) {
            item = new ItemStack(plugin.getConfig().getInt(path + ".id"));
        }
        if (plugin.getConfig().isSet(path + ".data")) {
            item.setDurability((short) plugin.getConfig().getInt(path + ".data"));
        }
        ItemMeta meta = item.getItemMeta();
        if (plugin.getConfig().isSet(path + ".name")) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(path + ".name")));
        }
        if (plugin.getConfig().isSet(path + ".lore")) {
            ArrayList<String> lore = new ArrayList<String>();
            for (String line : plugin.getConfig().getStringList(path + ".lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);

        return item;
    }
}



