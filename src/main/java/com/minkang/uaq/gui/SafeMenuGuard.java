package com.minkang.uaq.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.List;

public final class SafeMenuGuard implements Listener {
    private final Plugin plugin;
    private final List<String> keywords;

    public SafeMenuGuard(Plugin plugin, FileConfiguration cfg) {
        this.plugin = plugin;
        this.keywords = cfg.getStringList("uaq.security.menuKeywords");
    }

    private boolean isUaqMenu(Inventory top) {
        if (top == null) return false;
        String title = top.getTitle(); // 1.16.5 compatible getTitle()
        if (title == null) return false;
        for (String k : keywords) {
            if (k != null && !k.isEmpty() && title.contains(k)) return true;
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        if (!isUaqMenu(e.getView().getTopInventory())) return;

        // 전면 차단 (핫키/쉬프트/드롭/더블클릭 포함)
        e.setCancelled(true);

        // Top 인벤토리 클릭만 내부 핸들러로 디스패치 (선택)
        if (e.getClickedInventory() == e.getView().getTopInventory()) {
            final Player p = (Player) e.getWhoClicked();
            final int slot = e.getSlot();
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    UMenu.handleGuiClick(p, slot);
                } catch (Throwable t) {
                    // 안전 로그
                    plugin.getLogger().warning("GUI click handling error: " + t.getMessage());
                }
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrag(InventoryDragEvent e) {
        if (isUaqMenu(e.getView().getTopInventory())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onClose(InventoryCloseEvent e) {
        if (!isUaqMenu(e.getView().getTopInventory())) return;
        try {
            UMenu.onGuiClose((Player)e.getPlayer());
        } catch (Throwable ignored) {}
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e) {
        if (isUaqMenu(e.getPlayer().getOpenInventory().getTopInventory())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (isUaqMenu(e.getPlayer().getOpenInventory().getTopInventory())) e.setCancelled(true);
    }
}
