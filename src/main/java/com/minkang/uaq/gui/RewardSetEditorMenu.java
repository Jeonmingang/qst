
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.*; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.*;
public class RewardSetEditorMenu implements UMenu {
    private final String id; private final Inventory inv; public RewardSetEditorMenu(String id){ this.id=id; inv=Bukkit.createInventory(null,54, Text.color("&0세트 편집: "+id)); refresh(); }
    private void refresh(){ inv.clear(); RewardSet rs=UAQPlugin.get().rewards().get(id); int i=0; for(Reward r: rs.rewards){ switch(r.type){
        case MONEY: inv.setItem(i++, Items.button(Material.GOLD_INGOT, "&e돈 &f"+r.amount,"&7클릭: +1000","&7우클릭: 삭제")); break;
        case COMMAND: inv.setItem(i++, Items.button(Material.PAPER, "&b명령어","&7"+(r.command==null?"":r.command),"&7클릭: 샘플로 교체","&7우클릭: 삭제")); break;
        case ITEM: inv.setItem(i++, r.item!=null? r.item.clone():new ItemStack(Material.CHEST)); break; } }
        inv.setItem(45, Items.button(Material.ARROW, "&7<- 목록")); inv.setItem(49, Items.button(Material.LIME_WOOL, "&a저장")); inv.setItem(50, Items.button(Material.YELLOW_WOOL, "&e아이템 보상 편집","&7클릭: 입력창 열기"));
        inv.setItem(51, Items.button(Material.GOLD_INGOT, "&6돈 보상 추가","&7+1000 기본")); inv.setItem(52, Items.button(Material.PAPER, "&b명령어 보상 추가","&7broadcast {player} ...")); inv.setItem(53, Items.button(Material.RED_WOOL, "&c세트 삭제")); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){ Player p=(Player)e.getWhoClicked(); int slot=e.getSlot(); if(slot==45){ MenuListener.open(p,new RewardSetListMenu()); return; }
        if(slot==49){ UAQPlugin.get().rewards().save(); p.sendMessage(Text.color("&a저장 완료.")); return; } if(slot==51){ Reward r=new Reward(); r.type=RewardType.MONEY; r.amount=1000; UAQPlugin.get().rewards().get(id).rewards.add(r); refresh(); p.updateInventory(); return; }
        if(slot==52){ Reward r=new Reward(); r.type=RewardType.COMMAND; r.command="broadcast {player} got reward!"; UAQPlugin.get().rewards().get(id).rewards.add(r); refresh(); p.updateInventory(); return; }
        if(slot==50){ p.closeInventory(); ItemInputMenu.openForRewardSet(p,id); return; } if(slot==53){ UAQPlugin.get().rewards().delete(id); p.sendMessage(Text.color("&c세트 삭제됨.")); MenuListener.open(p,new RewardSetListMenu()); return; }
        RewardSet rs=UAQPlugin.get().rewards().get(id); if(slot>=0 && slot<rs.rewards.size()){ Reward r=rs.rewards.get(slot); if(e.isRightClick()){ rs.rewards.remove(slot); refresh(); p.updateInventory(); return; }
            switch(r.type){ case MONEY: r.amount+=1000; refresh(); p.updateInventory(); break; case COMMAND: r.command="give {player} minecraft:diamond 1"; refresh(); p.updateInventory(); break;
                case ITEM: if(e.getCursor()!=null && e.getCursor().getType()!=Material.AIR){ r.item=e.getCursor().clone(); refresh(); p.updateInventory(); } break; } } }
}
