package com.minkang.uaq.gui;

import com.minkang.uaq.util.Items;
import com.minkang.uaq.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class AdminMenu implements UMenu {
    private final Inventory inv;

    public AdminMenu(){
        this.inv = Bukkit.createInventory(null, 27, Text.color("&0UAQ 관리자"));
        inv.setItem(11, Items.button(Material.CHEST, "&e보상 세트 미리보기", "&7임시 기능"));
        inv.setItem(13, Items.button(Material.BOOK, "&a리로드", "&7설정/보상 리로드"));
        inv.setItem(15, Items.button(Material.BARRIER, "&c닫기"));
        MenuRegistry.track(inv);
    }

    @Override
    public Inventory getInventory() { return inv; }

    @Override
    public void onClick(InventoryClickEvent e){
        Player p = (Player)e.getWhoClicked();
        switch (e.getSlot()){
            case 11:
                p.sendMessage(Text.color("&7(미구현) 보상 세트 미리보기"));
                break;
            case 13:
                p.sendMessage(Text.color("&a리로드 완료."));
                p.closeInventory();
                break;
            case 15:
                p.closeInventory();
                break;
            default: break;
        }
    }
}
