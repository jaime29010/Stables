package com.hemmingfield.stables.listeners;

import com.hemmingfield.stables.managers.MerchantManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnload implements Listener {
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (MerchantManager.getInstance().isVillagerChunk(event.getChunk())) {
            event.setCancelled(true);
        }
    }
}
