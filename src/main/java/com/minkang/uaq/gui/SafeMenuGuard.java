package com.minkang.uaq.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SafeMenuGuard implements Listener {

    private boolean isUaqMenu(org.bukkit.inventory.Inventory top){
        return top != null && MenuRegistry.isTracked(top);
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e){
        if (isUaqMenu(e.getView().getTopInventory())){
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrag(InventoryDragEvent e){
        if (isUaqMenu(e.getView().getTopInventory())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        MenuRegistry.untrack(e.getInventory());
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e){
        if (isUaqMenu(e.getPlayer().getOpenInventory().getTopInventory())){
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent e){
        if (isUaqMenu(e.getPlayer().getOpenInventory().getTopInventory())){
            e.setCancelled(true);
        }
    }
}
