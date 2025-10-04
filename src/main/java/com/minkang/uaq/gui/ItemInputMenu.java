
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.*; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.*;
public class ItemInputMenu implements UMenu {
    private final Inventory inv; private final String setId; public ItemInputMenu(String setId){ this.setId=setId; inv=Bukkit.createInventory(null,27, com.minkang.uaq.util.Text.color("&0아이템 보상 넣기")); inv.setItem(26, Items.button(Material.LIME_WOOL, "&a저장")); }
    public static void openForRewardSet(Player p, String setId){ MenuListener.open(p, new ItemInputMenu(setId)); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){ if(e.getSlot()==26){ Player p=(Player)e.getWhoClicked(); RewardSet rs=UAQPlugin.get().rewards().get(setId); rs.rewards.removeIf(r-> r.type==RewardType.ITEM);
        for(int i=0;i<26;i++){ ItemStack it=inv.getItem(i); if(it!=null && it.getType()!=Material.AIR){ Reward r=new Reward(); r.type=RewardType.ITEM; r.item=it.clone(); rs.rewards.add(r);} } UAQPlugin.get().rewards().save(); p.sendMessage(Text.color("&a아이템 보상 저장됨.")); MenuListener.open(p,new RewardSetEditorMenu(setId)); } }
}
