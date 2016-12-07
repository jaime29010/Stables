package com.hemmingfield.stables;

import com.hemmingfield.stables.command.CommandManager;
import com.hemmingfield.stables.gui.PurchaseMenu;
import com.hemmingfield.stables.listeners.*;
import com.hemmingfield.stables.managers.MerchantManager;
import com.hemmingfield.stables.managers.StableManager;
import com.hemmingfield.stables.util.Language;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Stables extends JavaPlugin {
    private static Plugin pl;
    private Economy economy;

    public static Plugin getPlugin() {
        return pl;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> service = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (service != null) {
            economy = service.getProvider();
        }
        return economy != null;
    }

    @SuppressWarnings("deprecation")
    public void onEnable() {
        pl = this;
        setupEconomy();

        saveDefaultConfig();

        Language.loadMessages(this);

        new PurchaseMenu(this);

        Bukkit.getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getPluginManager().registerEvents(new HorseMount(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClose(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDismount(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEntity(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new SelectionHandler(), this);
        Bukkit.getPluginManager().registerEvents(new EntitySpawn(), this);
        Bukkit.getPluginManager().registerEvents(new ChunkUnload(), this);
        getCommand("caballos").setExecutor(new CommandManager());

        Bukkit.getScheduler().runTaskLater(this, new BukkitRunnable() {
            public void run() {
                MerchantManager.getInstance().load(Stables.this);
                StableManager.getInstance().load(Stables.this);
            }
        }, 200L);
        getLogger().info("Scheduled to load all merchants and stable data in 10 seconds.");
    }

    @Override
    public void onDisable() {
        StableManager.getInstance().save(this);

        MerchantManager.getInstance().save(this);
        saveConfig();
    }

    public Economy getEconomy() {
        return economy;
    }
}
