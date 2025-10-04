
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.configuration.file.FileConfiguration; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.event.inventory.ClickType; import org.bukkit.inventory.Inventory;
import java.time.LocalDate; import java.util.*;
public class AttendanceMenu implements UMenu {
    private final Player player; private final Inventory inv; private final FileConfiguration cfg; private int page=0;
    public AttendanceMenu(Player p){ this.player=p; this.cfg=UAQPlugin.get().getConfig(); inv=Bukkit.createInventory(null,27, Text.color("&0출석체크")); refresh(); }
    private void refresh(){
        inv.clear();
        int streak=UAQPlugin.get().attendance().getStreak(player.getUniqueId());
        boolean can=UAQPlugin.get().attendance().canClaim(player.getUniqueId());
        String resetLore = "&7다음 리셋: &f"+UAQPlugin.get().attendance().dates().fmtTime(UAQPlugin.get().attendance().dates().nextReset());
        inv.setItem(10, com.minkang.uaq.util.Items.button(Material.CLOCK, "&e연속 출석: "+streak, resetLore));
        // points & shop
        int pts = UAQPlugin.get().attendance().getPoints(player.getUniqueId());
        inv.setItem(11, com.minkang.uaq.util.Items.button(Material.DRAGON_BREATH, "&d포인트: &f"+pts, "&7좌클릭: 상점 열기"));
        // calendar 14 days (paged by page 0/1)
        LocalDate today = UAQPlugin.get().attendance().dates().todayGameDate();
        java.util.List<String> dates = UAQPlugin.get().attendance().getDates(player.getUniqueId());
        for(int i=0;i<7;i++){
            int offset = (page==0? 6-i : 13-i);
            LocalDate d = today.minusDays(offset);
            String ds = d.toString();
            boolean claimed = dates!=null && dates.contains(ds);
            inv.setItem(9+i, com.minkang.uaq.util.Items.button(claimed?Material.LIME_STAINED_GLASS_PANE:Material.GRAY_STAINED_GLASS_PANE, (claimed?"&a":"&7")+ds));
        }
        inv.setItem(18, com.minkang.uaq.util.Items.button(Material.ARROW, "&7이전"));
        inv.setItem(26, com.minkang.uaq.util.Items.button(Material.ARROW, "&7다음"));
        int prot = UAQPlugin.get().attendance().getProtector(player.getUniqueId());
        inv.setItem(12, com.minkang.uaq.util.Items.button(Material.SHIELD, "&b보호권: &f"+prot, "&7좌클릭: 포인트 구매", "&7우클릭: 돈으로 구매"));
        inv.setItem(16, com.minkang.uaq.util.Items.button(can?Material.LIME_WOOL:Material.GRAY_WOOL, can?"&a오늘 출석 받기":"&7이미 받음","&7클릭하여 보상 수령"));
    }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        if(e.getSlot()==18){ page = Math.max(0, page-1); refresh(); ((Player)e.getWhoClicked()).updateInventory(); return; }
        if(e.getSlot()==26){ page = Math.min(1, page+1); refresh(); ((Player)e.getWhoClicked()).updateInventory(); return; }
        if(e.getSlot()==11){ MenuListener.open((Player)e.getWhoClicked(), new PointShopMenu((Player)e.getWhoClicked())); return; }
        if(e.getSlot()==12){
            Player p=(Player)e.getWhoClicked();
            int pcost = UAQPlugin.get().getConfig().getInt("protector.costPoints", 0);
            double mcost = UAQPlugin.get().getConfig().getDouble("protector.costMoney", 0);
            if(e.getClick()==ClickType.RIGHT && mcost>0){
                if(UAQPlugin.get().attendance().buyProtectorWithMoney(p, mcost)) p.sendMessage(Text.color("&b보호권 구매(돈) 완료.")); else p.sendMessage(Text.color("&c잔액 부족."));
            } else {
                if(UAQPlugin.get().attendance().buyProtectorWithPoints(p.getUniqueId(), pcost)) p.sendMessage(Text.color("&b보호권 구매(포인트) 완료.")); else p.sendMessage(Text.color("&c포인트가 부족합니다."));
            }
            refresh(); p.updateInventory(); return;
        }
        if(e.getSlot()==16){
            Player p=(Player)e.getWhoClicked();
            if(!UAQPlugin.get().attendance().canClaim(p.getUniqueId())){ p.sendMessage(Text.color(UAQPlugin.get().getConfig().getString("messages.alreadyClaimed"))); return; }
            int streak=UAQPlugin.get().attendance().claim(p.getUniqueId());
            p.sendMessage(Text.color(UAQPlugin.get().getConfig().getString("messages.attendanceClaimed").replace("{streak}", String.valueOf(streak))));
            int cycle=UAQPlugin.get().getConfig().getInt("attendance.cycleDays",7); int day=(streak-1)%cycle+1; String setId=UAQPlugin.get().getConfig().getString("attendance.dailyRewards."+day,"daily_"+day);
            UAQPlugin.get().rewards().applySet(p, setId);
            UAQPlugin.get().party().onClaim(p);
            if(UAQPlugin.get().getConfig().getBoolean("ux.titles.enabled", true)) p.sendTitle(Text.color("&a출석 보상 지급!"), Text.color("&fDay "+day+" 수령"), 10,40,10);
            if (UAQPlugin.get().getConfig().isConfigurationSection("attendance.streakMilestones"))
                for (String key: UAQPlugin.get().getConfig().getConfigurationSection("attendance.streakMilestones").getKeys(false)){
                    try{ int ms=Integer.parseInt(key); if(streak==ms){ String sid=UAQPlugin.get().getConfig().getString("attendance.streakMilestones."+key); UAQPlugin.get().rewards().applySet(p, sid);} } catch(Exception ignored){}
                }
            UAQPlugin.get().attendance().save();
            int px=UAQPlugin.get().getConfig().getInt("pass.xpOnAttendance", 5); com.minkang.uaq.UAQPlugin.get().pass().addXP(p.getUniqueId(), px); com.minkang.uaq.UAQPlugin.get().pass().checkLevelUp(p);
            
            refresh(); p.updateInventory();
        }
    }
}
