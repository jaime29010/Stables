package com.hemmingfield.stables.listeners;

import com.hemmingfield.stables.Stables;
import com.hemmingfield.stables.gui.PurchaseMenu;
import com.hemmingfield.stables.util.IdleHorse;
import com.hemmingfield.stables.util.Language;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InventoryClick implements Listener {
    private Stables plugin;

    public InventoryClick(Stables plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTopInventory() != null && event.getView().getTopInventory() instanceof HorseInventory) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SADDLE) {
                event.setCancelled(true);
            }
        }
        if ((PurchaseMenu.getInstance().isViewing(player)) &&
                (event.getClickedInventory() == player.getOpenInventory().getTopInventory())) {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (this.plugin.getConfig().isSet("gui.items." + slot)) {
                List<String> ownedTypes = new ArrayList<String>();
                if (this.plugin.getConfig().isSet("bought." + player.getUniqueId())) {
                    ownedTypes = this.plugin.getConfig().getStringList("bought." + player.getUniqueId());
                }
                if (this.plugin.getConfig().getBoolean("gui.items." + slot + ".isVip") && !player.hasPermission("horses.vip")) {
                    player.sendMessage(ChatColor.RED + "Necesitas ser vip para poder comprar este caballo");
                    return;
                }
                double cost = 0.0D;
                if (this.plugin.getConfig().isSet("gui.items." + slot + ".cost")) {
                    cost = this.plugin.getConfig().getDouble("gui.items." + slot + ".cost");
                }
                if (!ownedTypes.contains(this.plugin.getConfig().getString("gui.items." + slot + ".type"))) {
                    if (!plugin.getEconomy().has(player, cost)) {
                        Language.ERROR_INSUFFICIENT_FUNDS.sendTo(player);
                        return;
                    }

                    EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, cost);
                    if (response.transactionSuccess()) {
                        ownedTypes.add(this.plugin.getConfig().getString("gui.items." + slot + ".type"));
                        this.plugin.getConfig().set("bought." + player.getUniqueId(), ownedTypes);
                    }
                }
                Variant v = org.bukkit.entity.Horse.Variant.valueOf(this.plugin.getConfig().getString("gui.items." + slot + ".variant"));
                org.bukkit.entity.Horse.Color c = org.bukkit.entity.Horse.Color.valueOf(this.plugin.getConfig().getString("gui.items." + slot + ".color"));
                Style s = org.bukkit.entity.Horse.Style.valueOf(this.plugin.getConfig().getString("gui.items." + slot + ".style"));
                if (this.plugin.getConfig().getBoolean("gui.items." + slot + ".isNormal")) {
                    Random rand = new Random();
                    switch (rand.nextInt(5)) {
                        case 0:
                            c = Horse.Color.BROWN;
                            break;
                        case 1:
                            c = Horse.Color.CHESTNUT;
                            break;
                        case 2:
                            c = Horse.Color.CREAMY;
                            break;
                        case 3:
                            c = Horse.Color.DARK_BROWN;
                            break;
                        case 4:
                            c = Horse.Color.GRAY;
                            break;
                    }
                    switch (rand.nextInt(3)) {
                        case 0:
                            s = Horse.Style.BLACK_DOTS;
                            break;
                        case 1:
                            s = Horse.Style.NONE;
                            break;
                        case 2:
                            s = Horse.Style.WHITE_DOTS;
                            break;
                    }
                }
                IdleHorse horse = new IdleHorse(this.plugin.getConfig().getDouble("gui.items." + slot + ".speed"),
                        this.plugin.getConfig().getDouble("gui.items." + slot + ".jump"),
                        0,
                        s,
                        c,
                        v,
                        new ItemStack(Material.AIR),
                        "",
                        false,
                        this.plugin.getConfig().getBoolean("gui.items." + slot + ".isOnFire"),
                        this.plugin.getConfig().getInt("gui.items." + slot + ".nauseaTime"),
                        this.plugin.getConfig().getInt("gui.items." + slot + ".blindnessTime"),
                        this.plugin.getConfig().getBoolean("gui.items." + slot + ".isVip"),
                        this.plugin.getConfig().getBoolean("gui.items." + slot + ".flies"),
                        this.plugin.getConfig().getInt("gui.items." + slot + ".speedTime"));
                horse.spawn(player);
                player.closeInventory();
            }
        }
    }
}



