
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
public class SettingsMenu implements UMenu {
    private final Player player; private final Inventory inv;
    public SettingsMenu(Player p){ this.player=p; this.inv=Bukkit.createInventory(null, 27, Text.color("&0개인 설정")); refresh(); }
    private void refresh(){
        inv.clear();
        boolean hud=UAQPlugin.get().prefs().get(player.getUniqueId(), "hud", true);
        boolean auto=UAQPlugin.get().getConfig().getBoolean("roulette.autoOpen", true) && UAQPlugin.get().prefs().get(player.getUniqueId(), "rouletteAuto", true);
        boolean boss=UAQPlugin.get().prefs().get(player.getUniqueId(), "bossbar", true);
        inv.setItem(10, com.minkang.uaq.util.Items.button(hud?Material.LIME_DYE:Material.GRAY_DYE, "&eHUD 스코어보드: "+(hud?"&aON":"&7OFF")));
        inv.setItem(12, com.minkang.uaq.util.Items.button(auto?Material.LIME_DYE:Material.GRAY_DYE, "&e출석 후 룰렛 자동열기: "+(auto?"&aON":"&7OFF")));
        inv.setItem(14, com.minkang.uaq.util.Items.button(boss?Material.LIME_DYE:Material.GRAY_DYE, "&e퀘스트 보스바: "+(boss?"&aON":"&7OFF")));
        inv.setItem(18, com.minkang.uaq.util.Items.button(Material.ARROW, "&7<- 닫기"));
    }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        if(e.getSlot()==18){ player.closeInventory(); return; }
        if(e.getSlot()==10){ boolean v=!UAQPlugin.get().prefs().get(player.getUniqueId(), "hud", true); UAQPlugin.get().prefs().set(player.getUniqueId(),"hud",v); if(v) UAQPlugin.get().getScoreboardService().update(player); else player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); }
        if(e.getSlot()==12){ boolean v=!UAQPlugin.get().prefs().get(player.getUniqueId(), "rouletteAuto", true); UAQPlugin.get().prefs().set(player.getUniqueId(),"rouletteAuto",v); }
        if(e.getSlot()==14){ boolean v=!UAQPlugin.get().prefs().get(player.getUniqueId(), "bossbar", true); UAQPlugin.get().prefs().set(player.getUniqueId(),"bossbar",v); if(!v) UAQPlugin.get().getTracker().unpin(player); }
        refresh(); player.updateInventory();
    }
}
