
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.Items; import com.minkang.uaq.util.Text;
import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
import java.util.*;
public class DexRewardClaimMenu implements UMenu {
    private final Player player; private final Inventory inv; private final java.util.List<Integer> pending;
    public DexRewardClaimMenu(Player p, java.util.List<Integer> pending){ this.player=p; this.pending=new java.util.ArrayList<>(pending); this.inv=Bukkit.createInventory(null, 54, Text.color("&0도감 보상 수령")); refresh(); }
    private void refresh(){ inv.clear(); int i=9; for(Integer t: pending){ String setId=com.minkang.uaq.UAQPlugin.get().dex().rewardSetFor(t); inv.setItem(i++, Items.button(Material.CHEST, "&e"+t+"% 보상 수령", "&7세트: "+setId)); } inv.setItem(49, Items.button(Material.LIME_WOOL, "&a모두 받기")); inv.setItem(45, Items.button(Material.ARROW, "&7<- 닫기")); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        if(e.getSlot()==45){ player.closeInventory(); return; }
        if(e.getSlot()==49){ for(int j=pending.size()-1;j>=0;j--){ Integer t=pending.remove(j); String setId=com.minkang.uaq.UAQPlugin.get().dex().rewardSetFor(t); if(setId!=null) com.minkang.uaq.UAQPlugin.get().rewards().applySet(player, setId); com.minkang.uaq.UAQPlugin.get().dex().addClaimed(player.getUniqueId(), t); } player.sendMessage(Text.color("&a도감 보상 모두 수령 완료.")); refresh(); player.updateInventory(); return; }
        int idx=e.getSlot()-9; if(idx<0 || idx>=pending.size()) return;
        Integer t=pending.remove(idx); String setId=com.minkang.uaq.UAQPlugin.get().dex().rewardSetFor(t);
        if(setId!=null) com.minkang.uaq.UAQPlugin.get().rewards().applySet(player, setId);
        com.minkang.uaq.UAQPlugin.get().dex().addClaimed(player.getUniqueId(), t);
        player.sendMessage(Text.color("&a도감 보상 수령: &e"+t+"%"));
        refresh(); player.updateInventory();
    }
}
