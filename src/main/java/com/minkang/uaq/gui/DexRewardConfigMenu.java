
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.RewardSet; import com.minkang.uaq.util.Items; import com.minkang.uaq.util.Text;
import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.event.inventory.ClickType; import org.bukkit.inventory.Inventory;
import java.util.*;
public class DexRewardConfigMenu implements UMenu {
    private final Player admin; private final Inventory inv; private java.util.List<Integer> ts=new java.util.ArrayList<>();
    public DexRewardConfigMenu(Player p){ this.admin=p; this.inv=Bukkit.createInventory(null, 54, Text.color("&0도감 보상 설정")); refresh(); }
    private void refresh(){
        inv.clear(); ts=new java.util.ArrayList<>(UAQPlugin.get().dex().thresholds()); java.util.Collections.sort(ts);
        int i=9; for(Integer t: ts){ String setId = UAQPlugin.get().dex().rewardSetFor(t); inv.setItem(i++, Items.button(Material.BOOK, "&e"+t+"% 보상", "&7세트: "+setId, "&8좌클릭: 다음 세트", "&8우클릭: 삭제")); }
        inv.setItem(48, Items.button(Material.PAPER, "&a+1% 추가"));
        inv.setItem(49, Items.button(Material.LIME_DYE, "&a+10% 추가"));
        inv.setItem(45, Items.button(Material.ARROW, "&7<- 닫기"));
    }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        Player p=(Player)e.getWhoClicked();
        if(e.getSlot()==45){ p.closeInventory(); return; }
        if(e.getSlot()==48 || e.getSlot()==49){
            int step = e.getSlot()==49? 10 : 1;
            for(int v=step; v<=100; v+=step){
                if(!ts.contains(v)){ String firstSet = UAQPlugin.get().rewards().allSets().isEmpty()? "daily_1" : UAQPlugin.get().rewards().allSets().get(0).id;
                    UAQPlugin.get().getConfig().set("dex.thresholds."+v, firstSet); UAQPlugin.get().saveConfig(); break; }
            }
            refresh(); p.updateInventory(); return;
        }
        int idx=e.getSlot()-9; if(idx<0 || idx>=ts.size()) return;
        Integer t = ts.get(idx);
        if(e.getClick()==ClickType.RIGHT){
            UAQPlugin.get().getConfig().set("dex.thresholds."+t, null); UAQPlugin.get().saveConfig(); refresh(); p.updateInventory(); return;
        }
        java.util.List<RewardSet> sets=UAQPlugin.get().rewards().allSets(); if(sets.isEmpty()) return;
        String cur = UAQPlugin.get().dex().rewardSetFor(t);
        int id = -1; for(int i=0;i<sets.size();i++) if(sets.get(i).id.equals(cur)) { id=i; break; }
        String nextSet = sets.get((id+1+sets.size())%sets.size()).id;
        UAQPlugin.get().getConfig().set("dex.thresholds."+t, nextSet); UAQPlugin.get().saveConfig();
        refresh(); p.updateInventory();
    }
}
