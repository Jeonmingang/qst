
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.Items; import com.minkang.uaq.util.Text;
import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
import java.util.*;
public class RankMenu implements UMenu {
    private final Player player; private final Inventory inv; private int mode=0; //0:weekly,1:season,2:streak
    public RankMenu(Player p){ this.player=p; this.inv=Bukkit.createInventory(null, 54, Text.color("&0랭킹")); refresh(); }
    private String name(java.util.UUID u){ org.bukkit.OfflinePlayer op=Bukkit.getOfflinePlayer(u); return (op!=null && op.getName()!=null)? op.getName() : u.toString().substring(0,8); }
    private void refresh(){
        inv.clear();
        java.util.List<java.util.Map.Entry<java.util.UUID,Integer>> list;
        if(mode==0){ list=UAQPlugin.get().lb().top("weekly", 10); inv.setItem(4, Items.button(Material.CLOCK, "&e주간 포인트 TOP 10")); }
        else if(mode==1){ list=UAQPlugin.get().lb().top("season", 10); inv.setItem(4, Items.button(Material.DRAGON_BREATH, "&d시즌 포인트 TOP 10")); }
        else { // streak from attendance data not stored as map; we approximate current online/offline not possible -> only show online for now or skip
            list=new java.util.ArrayList<>();
            for(org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()){ list.add(new java.util.AbstractMap.SimpleEntry<>(p.getUniqueId(), UAQPlugin.get().attendance().getStreak(p.getUniqueId()))); }
            list.sort((a,b)->Integer.compare(b.getValue(), a.getValue()));
            inv.setItem(4, Items.button(Material.SHIELD, "&b연속 출석 (온라인)"));
        }
        int i=9; int rank=1;
        for(java.util.Map.Entry<java.util.UUID,Integer> e: list){
            if(i>=9+27) break;
            inv.setItem(i++, Items.button(Material.PLAYER_HEAD, "&6#"+rank+" &f"+name(e.getKey()), "&7점수: &e"+e.getValue())); rank++;
        }
        inv.setItem(45, Items.button(Material.ARROW, "&7<- 닫기"));
        inv.setItem(49, Items.button(Material.PAPER, "&7모드 변경"));
    }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        if(e.getSlot()==45){ player.closeInventory(); return; }
        if(e.getSlot()==49){ mode=(mode+1)%3; refresh(); player.updateInventory(); }
    }
}
