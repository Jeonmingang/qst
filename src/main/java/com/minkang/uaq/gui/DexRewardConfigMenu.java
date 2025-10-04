package com.minkang.uaq.gui;

import com.minkang.uaq.util.Items;
import com.minkang.uaq.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class DexRewardConfigMenu implements UMenu {
    private final Player admin;
    private final Inventory inv;

    public DexRewardConfigMenu(Player p){
        this.admin = p;
        this.inv = Bukkit.createInventory(null, 27, Text.color("&0도감 보상 설정(간이)"));
        inv.setItem(13, Items.button(Material.BOOK, "&a준비중", "&7보상 매핑은 차기 버전에서"));
        MenuRegistry.track(inv);
    }

    @Override public Inventory getInventory(){ return inv; }

    @Override public void onClick(InventoryClickEvent e){
        if (!(e.getWhoClicked() instanceof Player)) return;
        ((Player)e.getWhoClicked()).closeInventory();
    }
}
