
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.configuration.ConfigurationSection; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
import java.util.*;
public class PointShopMenu implements UMenu {
    private final Player player; private final Inventory inv;
    public PointShopMenu(Player p){ this.player=p; this.inv=Bukkit.createInventory(null, 54, Text.color("&0출석 포인트 상점")); refresh(); }
    private void refresh(){ inv.clear();
        int points = UAQPlugin.get().attendance().getPoints(player.getUniqueId());
        inv.setItem(4, Items.button(Material.DRAGON_BREATH, "&d내 포인트: &f"+points));
        // items from config
        ConfigurationSection s = UAQPlugin.get().getConfig().getConfigurationSection("pointShop.items");
        if(s!=null){
            int i=9;
            for(String key: s.getKeys(false)){
                ConfigurationSection it = s.getConfigurationSection(key);
                String name = it.getString("name", key);
                int cost = it.getInt("costPoints", 0);
                String rewardSet = it.getString("rewardSet", "");
                String command = it.getString("command", "");
                inv.setItem(i++, Items.button(Material.EMERALD, "&a"+name, "&7가격: &d"+cost+"P", rewardSet.isEmpty()? "&7명령: "+command : "&7세트: "+rewardSet, "&8클릭: 구매"));
            }
        }
        // protector purchase shortcuts
        int pcost = UAQPlugin.get().getConfig().getInt("protector.costPoints", 0);
        double mcost = UAQPlugin.get().getConfig().getDouble("protector.costMoney", 0);
        inv.setItem(49, Items.button(Material.SHIELD, "&b결석 보호권 구매",
                "&7포인트: "+pcost+"P", "&7돈: "+(mcost>0? (int)mcost+" 원":"비활성")));
        inv.setItem(45, Items.button(Material.ARROW, "&7<- 뒤로"));
    }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        Player p=(Player)e.getWhoClicked();
        if(e.getSlot()==45){ MenuListener.open(p, new AttendanceMenu(p)); return; }
        if(e.getSlot()==49){
            int pcost = UAQPlugin.get().getConfig().getInt("protector.costPoints", 0);
            double mcost = UAQPlugin.get().getConfig().getDouble("protector.costMoney", 0);
            if(e.isRightClick() && mcost>0){
                if(UAQPlugin.get().attendance().buyProtectorWithMoney(p, mcost)) p.sendMessage(Text.color("&b보호권 구매(돈) 완료.")); else p.sendMessage(Text.color("&c잔액 부족."));
            } else {
                if(UAQPlugin.get().attendance().buyProtectorWithPoints(p.getUniqueId(), pcost)) p.sendMessage(Text.color("&b보호권 구매(포인트) 완료.")); else p.sendMessage(Text.color("&c포인트가 부족합니다."));
            }
            refresh(); p.updateInventory(); return;
        }
        // Items purchase by index inference
        ConfigurationSection s = UAQPlugin.get().getConfig().getConfigurationSection("pointShop.items");
        if(s==null) return;
        int idx = e.getSlot()-9;
        if(idx<0) return;
        java.util.List<String> keys = new java.util.ArrayList<>(s.getKeys(false));
        if(idx>=keys.size()) return;
        ConfigurationSection it = s.getConfigurationSection(keys.get(idx));
        int cost = it.getInt("costPoints", 0);
        if(!UAQPlugin.get().attendance().spendPoints(p.getUniqueId(), cost)){ p.sendMessage(Text.color("&c포인트 부족.")); return; }
        String rewardSet = it.getString("rewardSet", "");
        String command = it.getString("command", "");
        if(!rewardSet.isEmpty()) UAQPlugin.get().rewards().applySet(p, rewardSet);
        if(!command.isEmpty()) org.bukkit.Bukkit.dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), command.replace("{player}", p.getName()));
        p.sendMessage(Text.color("&a구매 완료! -"+cost+"P"));
        refresh(); p.updateInventory();
    }
}
