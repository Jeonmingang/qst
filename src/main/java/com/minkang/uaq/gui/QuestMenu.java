
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.Quest; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.event.inventory.ClickType; import org.bukkit.inventory.Inventory; import java.util.*;
public class QuestMenu implements UMenu {
    private final Player player; private final Inventory inv;
    public QuestMenu(Player p){ this.player=p; inv=Bukkit.createInventory(null,54, Text.color("&0일일 퀘스트")); refresh(); }
    private void refresh(){ inv.clear(); java.util.Set<String> as=UAQPlugin.get().quests().getAssigned(player.getUniqueId()); int i=0;
        for(String id: as){ Quest q=UAQPlugin.get().quests().pool().get(id); if(q==null) continue; int prog=UAQPlugin.get().quests().getProgress(player.getUniqueId(), id); boolean done=prog>=q.goal;
            java.util.List<String> preview=UAQPlugin.get().rewards().previewLines(q.rewardSet,3); java.util.List<String> lore=new java.util.ArrayList<>(); lore.add("&7"+q.description); lore.add("&7진행도: &b"+prog+"/"+q.goal);
            lore.add("&7보상 미리보기:"); for(String line: preview) lore.add(line); lore.add("&8좌클릭: 완료 시 수령 / 우클릭: 리롤 / 휠클릭: 추적 고정"); inv.setItem(i++, Items.button(done?Material.LIME_WOOL:Material.BOOK, (done?"&a[완료] ":"&e")+q.name, lore.toArray(new String[0]))); }
        inv.setItem(53, Items.button(Material.HOPPER, "&a완료 보상 일괄 수령")); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){ int idx=e.getSlot(); if(idx==53){ java.util.List<String> all=new java.util.ArrayList<>(UAQPlugin.get().quests().getAssigned(player.getUniqueId())); int claimed=0;
            for(String id2: all){ Quest q2=UAQPlugin.get().quests().pool().get(id2); int p2=UAQPlugin.get().quests().getProgress(player.getUniqueId(), id2); if(p2>=q2.goal){ UAQPlugin.get().rewards().applySet(player, q2.rewardSet); UAQPlugin.get().quests().getAssigned(player.getUniqueId()).remove(id2); claimed++; } }
            if(claimed>0){ player.playSound(player.getLocation(), org.bukkit.Sound.valueOf(UAQPlugin.get().getConfig().getString("ux.sounds.claim","ENTITY_PLAYER_LEVELUP")), 1f,1f);
                if(UAQPlugin.get().getConfig().getBoolean("ux.titles.enabled", true)) player.sendTitle(Text.color(UAQPlugin.get().getConfig().getString("ux.titles.completeTitle","&a퀘스트 완료!")), Text.color("&f"+claimed+"개 보상 수령"), 10,40,10); }
            else player.sendMessage(Text.color("&7수령 가능한 완료 퀘스트가 없습니다.")); refresh(); player.updateInventory(); return; }
        java.util.List<String> list=new java.util.ArrayList<>(UAQPlugin.get().quests().getAssigned(player.getUniqueId())); if(idx>=0 && idx<list.size()){ String id=list.get(idx); Quest q=UAQPlugin.get().quests().pool().get(id); int prog=UAQPlugin.get().quests().getProgress(player.getUniqueId(), id);
            if(e.isRightClick()){ if(UAQPlugin.get().quests().reroll(player, id)){ player.sendMessage(Text.color("&a퀘스트가 리롤되었습니다.")); refresh(); player.updateInventory(); } else player.sendMessage(Text.color("&c리롤 불가(남은 횟수/설정/잔액 확인).")); }
            else if(e.getClick()==ClickType.MIDDLE){ com.minkang.uaq.UAQPlugin.get().getTracker().pin(player, id); player.sendMessage(com.minkang.uaq.util.Text.color("&b추적 고정: ")+q.name); }
            else if(prog>=q.goal){ UAQPlugin.get().rewards().applySet(player, q.rewardSet); player.playSound(player.getLocation(), org.bukkit.Sound.valueOf(UAQPlugin.get().getConfig().getString("ux.sounds.claim","ENTITY_PLAYER_LEVELUP")), 1f,1f);
                if(UAQPlugin.get().getConfig().getBoolean("ux.titles.enabled", true)) player.sendTitle(Text.color(UAQPlugin.get().getConfig().getString("ux.titles.completeTitle","&a퀘스트 완료!")), Text.color(UAQPlugin.get().getConfig().getString("ux.titles.completeSub","&f보상을 수령했습니다.")), 10,40,10);
                player.sendMessage(Text.color("&a보상 수령: &f"+q.rewardSet)); UAQPlugin.get().quests().getAssigned(player.getUniqueId()).remove(id); UAQPlugin.get().quests().savePlayers(); refresh(); player.updateInventory(); }
            else player.sendMessage(Text.color("&c아직 완료되지 않았습니다.")); } }
}
