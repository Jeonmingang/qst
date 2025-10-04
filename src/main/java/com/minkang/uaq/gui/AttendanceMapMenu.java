
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.RewardSet; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.configuration.file.FileConfiguration; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
public class AttendanceMapMenu implements UMenu {
    private final Inventory inv; private final FileConfiguration cfg; public AttendanceMapMenu(){ this.cfg=UAQPlugin.get().getConfig(); inv=Bukkit.createInventory(null,54, Text.color("&0출석 리워드 매핑")); refresh(); }
    private void refresh(){ inv.clear(); int cycle=cfg.getInt("attendance.cycleDays",7); for(int d=1; d<=cycle; d++){ String id=cfg.getString("attendance.dailyRewards."+d, "daily_"+d);
        inv.setItem(d-1, Items.button(Material.CLOCK, "&eDay "+d, "&7세트: "+id, "&7클릭: 다음 세트로")); } inv.setItem(45, Items.button(Material.ARROW, "&7<- 뒤로")); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){ Player p=(Player)e.getWhoClicked(); if(e.getSlot()==45){ MenuListener.open(p,new AdminMenu()); return; }
        int cycle=cfg.getInt("attendance.cycleDays",7); if(e.getSlot()>=0 && e.getSlot()<cycle){ int day=e.getSlot()+1; java.util.List<String> ids=new java.util.ArrayList<>(); for(RewardSet rs: UAQPlugin.get().rewards().allSets()) ids.add(rs.id);
            if(ids.isEmpty()) return; String cur=cfg.getString("attendance.dailyRewards."+day, ids.get(0)); int idx=ids.indexOf(cur); String next=ids.get((idx+1)%ids.size()); cfg.set("attendance.dailyRewards."+day, next); UAQPlugin.get().saveConfig(); refresh(); p.updateInventory(); } }
}
