package com.minkang.uaq.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface UMenu {
    Inventory getInventory();
    default void onClick(InventoryClickEvent e) {}
    default void onClose(org.bukkit.event.inventory.InventoryCloseEvent e) {}
}
