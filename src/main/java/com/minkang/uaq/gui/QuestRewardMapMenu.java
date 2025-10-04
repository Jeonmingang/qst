
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.Quest; import com.minkang.uaq.model.RewardSet; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
import java.util.*;
public class QuestRewardMapMenu implements UMenu {
    private final Inventory inv; private final java.util.List<String> ids=new java.util.ArrayList<>();
    public QuestRewardMapMenu(){ inv=Bukkit.createInventory(null,54, Text.color("&0퀘스트 보상 매핑")); ids.addAll(UAQPlugin.get().quests().pool().keySet()); refresh(); }
    private void refresh(){ inv.clear(); int i=0; for(String id: ids){ Quest q=UAQPlugin.get().quests().pool().get(id); if(q==null) continue;
            inv.setItem(i++, com.minkang.uaq.util.Items.button(Material.BOOK, "&e"+q.name+" &7("+q.id+")", "&7보상: "+q.rewardSet, "&7클릭: 다음 세트")); }
        inv.setItem(45, com.minkang.uaq.util.Items.button(Material.ARROW, "&7<- 뒤로")); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){ Player p=(Player)e.getWhoClicked(); if(e.getSlot()==45){ MenuListener.open(p,new AdminMenu()); return; }
        if(e.getSlot()<0 || e.getSlot()>=ids.size()) return; String id=ids.get(e.getSlot()); Quest q=UAQPlugin.get().quests().pool().get(id);
        java.util.List<String> sets=new java.util.ArrayList<>(); for(RewardSet rs: UAQPlugin.get().rewards().allSets()) sets.add(rs.id);
        if(sets.isEmpty()) return; int idx=sets.indexOf(q.rewardSet); q.rewardSet = sets.get((idx+1+sets.size())%sets.size()); UAQPlugin.get().quests().saveQuestCfg(); refresh(); p.updateInventory(); }
}
