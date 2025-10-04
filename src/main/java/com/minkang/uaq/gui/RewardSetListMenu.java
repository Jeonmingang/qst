
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.RewardSet; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.*;
public class RewardSetListMenu implements UMenu {
    private final Inventory inv; public RewardSetListMenu(){ inv=Bukkit.createInventory(null,54, Text.color("&0보상 세트 목록")); refresh(); }
    private void refresh(){ inv.clear(); int i=0; for(RewardSet rs: UAQPlugin.get().rewards().allSets()) inv.setItem(i++, Items.button(Material.CHEST, "&e"+rs.name+" &7("+rs.id+")","&7클릭: 편집","&8보상수: "+rs.rewards.size()));
        inv.setItem(53, Items.button(Material.LIME_WOOL, "&a새 세트 추가","&7클릭하여 ID 자동생성")); inv.setItem(45, Items.button(Material.ARROW, "&7<- 뒤로")); UAQPlugin.get().rewards().save(); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){ Player p=(Player)e.getWhoClicked(); if(e.getSlot()==45){ MenuListener.open(p,new AdminMenu()); return; }
        if(e.getSlot()==53){ String id="set_"+(System.currentTimeMillis()%100000); UAQPlugin.get().rewards().ensure(id); UAQPlugin.get().rewards().save(); MenuListener.open(p,new RewardSetEditorMenu(id)); return; }
        int index=e.getSlot(); java.util.List<RewardSet> list=new java.util.ArrayList<>(UAQPlugin.get().rewards().allSets()); if(index>=0 && index<list.size()) MenuListener.open(p,new RewardSetEditorMenu(list.get(index).id)); }
}
