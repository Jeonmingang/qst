
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.Items; import com.minkang.uaq.util.Text;
import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
public class PassMenu implements UMenu {
    private final Player player; private final Inventory inv;
    public PassMenu(Player p){ this.player=p; this.inv=Bukkit.createInventory(null, 54, Text.color("&0시즌 패스")); refresh(); }
    private void refresh(){
        inv.clear();
        int lv=UAQPlugin.get().pass().getLevel(player.getUniqueId());
        int xp=UAQPlugin.get().pass().getXP(player.getUniqueId());
        boolean premium=UAQPlugin.get().pass().isPremium(player.getUniqueId());
        inv.setItem(4, Items.button(Material.NETHER_STAR, "&eLv."+lv+" &7/ &fXP "+xp, premium? "&d프리미엄 활성화":"&7프리미엄 미보유"));
        inv.setItem(49, Items.button(Material.BOOK, "&7설명", "&7퀘/출석/포인트로 패스 XP 획득", "&7좌클릭: 새로고침"));
        inv.setItem(45, Items.button(Material.ARROW, "&7<- 닫기"));
    }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        if(e.getSlot()==45){ player.closeInventory(); return; }
        if(e.getSlot()==49){ refresh(); player.updateInventory(); }
    }
}
