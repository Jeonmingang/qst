package com.minkang.uaq.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuListener implements Listener {
    private static final Map<UUID, UMenu> OPEN = new HashMap<>();

    public static void open(Player p, UMenu menu){
        OPEN.put(p.getUniqueId(), menu);
        p.openInventory(menu.getInventory());
    }

    @EventHandler
    public void click(InventoryClickEvent e){
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player)e.getWhoClicked();
        UMenu m = OPEN.get(p.getUniqueId());
        if (m == null) return;
        if (e.getView().getTopInventory().equals(m.getInventory())){
            e.setCancelled(true);
            m.onClick(e);
        }
    }

    @EventHandler
    public void close(InventoryCloseEvent e){
        UMenu m = OPEN.remove(e.getPlayer().getUniqueId());
        if (m != null) m.onClose(e);
    }
}
