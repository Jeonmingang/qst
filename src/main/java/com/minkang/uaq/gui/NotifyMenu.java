
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.Items; import com.minkang.uaq.util.Text;
import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
public class NotifyMenu implements UMenu {
    private final Player player; private final Inventory inv;
    public NotifyMenu(Player p){ this.player=p; this.inv=Bukkit.createInventory(null, 54, Text.color("&0보상 로그")); refresh(); }
    private void refresh(){ inv.clear(); java.util.List<String> lines=UAQPlugin.get().notifySrv().list(player.getUniqueId()); int i=9; int idx=lines.size()-1;
        while(i<9+36 && idx>=0){ inv.setItem(i++, Items.button(Material.PAPER, "&f"+lines.get(idx))); idx--; } inv.setItem(45, Items.button(Material.ARROW, "&7<- 닫기")); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){ if(e.getSlot()==45){ player.closeInventory(); } }
}
