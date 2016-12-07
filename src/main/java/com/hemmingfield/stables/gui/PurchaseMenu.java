package com.hemmingfield.stables.gui;

import com.hemmingfield.stables.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PurchaseMenu
        implements Listener {
    private static PurchaseMenu instance = null;
    private Plugin plugin;
    private ArrayList<Player> viewing = new ArrayList<Player>();

    public PurchaseMenu(Plugin plugin) {
        instance = this;

        this.plugin = plugin;
    }

    public static PurchaseMenu getInstance() {
        return instance;
    }

    public boolean isViewing(Player player) {
        return this.viewing.contains(player);
    }

    public void display(Player player) {
        List<String> ownedTypes = new ArrayList<String>();
        if (this.plugin.getConfig().isSet("bought." + player.getUniqueId())) {
            ownedTypes = this.plugin.getConfig().getStringList("bought." + player.getUniqueId());
        }
        Inventory gui = Bukkit.createInventory(null, this.plugin.getConfig().getInt("gui.size"), ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("gui.title")));
        for (int i = 0; i < gui.getSize(); i++) {
            if (this.plugin.getConfig().isSet("gui.items." + i)) {
                ItemStack item = ItemBuilder.getItem(this.plugin, "gui.items." + i + ".item");
                if (ownedTypes.contains(this.plugin.getConfig().getString("gui.items." + i + ".type"))) {
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(meta.getDisplayName() + this.plugin.getConfig().getString("gui.boughtPrefix").replace("&", "�"));
                    item.setItemMeta(meta);
                }
                gui.setItem(i, item);
            }
        }
        player.openInventory(gui);

        this.viewing.add(player);
    }

    public void remove(Player player) {
        this.viewing.remove(player);
    }
}



