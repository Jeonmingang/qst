
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
import java.time.*; import java.util.*;
public class CalendarMenu implements UMenu {
    private final Player player; private final Inventory inv; private YearMonth month;
    public CalendarMenu(Player p){ this.player=p; this.month=YearMonth.now(java.time.ZoneId.of(UAQPlugin.get().getConfig().getString("timezone","Asia/Seoul"))); this.inv=Bukkit.createInventory(null, 54, Text.color("&0월간 달력")); refresh(); }
    private void refresh(){
        inv.clear();
        java.util.List<String> dates = UAQPlugin.get().attendance().getDates(player.getUniqueId());
        LocalDate first = month.atDay(1);
        int start = first.getDayOfWeek().getValue()%7; // 0:Sun
        int days = month.lengthOfMonth();
        inv.setItem(4, Items.button(Material.CLOCK, "&e"+month.getYear()+"-"+String.format("%02d",month.getMonthValue())));
        for(int d=1; d<=days; d++){
            int slot = 9 + start + (d-1);
            boolean claimed = dates!=null && dates.contains(month.atDay(d).toString());
            inv.setItem(slot, Items.button(claimed?Material.LIME_STAINED_GLASS_PANE:Material.GRAY_STAINED_GLASS_PANE, (claimed?"&a":"&7")+d+"일"));
        }
        inv.setItem(45, Items.button(Material.ARROW, "&7<- 이전달"));
        inv.setItem(53, Items.button(Material.ARROW, "&7다음달 ->"));
    }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        if(e.getSlot()==45){ month=month.minusMonths(1); refresh(); ((Player)e.getWhoClicked()).updateInventory(); return; }
        if(e.getSlot()==53){ month=month.plusMonths(1); refresh(); ((Player)e.getWhoClicked()).updateInventory(); return; }
    }
}
