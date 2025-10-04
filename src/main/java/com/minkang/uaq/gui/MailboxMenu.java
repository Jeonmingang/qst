
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.Items; import com.minkang.uaq.util.Text;
import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory; import org.bukkit.inventory.ItemStack;
public class MailboxMenu implements UMenu {
    private final Player player; private final Inventory inv;
    public MailboxMenu(Player p){ this.player=p; this.inv=Bukkit.createInventory(null,54, Text.color("&0UAQ 우편함")); refresh(); }
    private void refresh(){ inv.clear(); java.util.List<ItemStack> list=UAQPlugin.get().mail().get(player.getUniqueId()); int i=0; for(ItemStack it: list){ inv.setItem(i++, it.clone()); if(i>=45) break; }
        inv.setItem(49, Items.button(Material.LIME_WOOL, "&a모두 받기")); inv.setItem(45, Items.button(Material.ARROW, "&7<- 닫기")); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){
        if(e.getSlot()==49){ int got=UAQPlugin.get().mail().claimAll(player); player.sendMessage(Text.color("&a우편 수령: &f"+got+"개")); refresh(); player.updateInventory(); return; }
        if(e.getSlot()==45){ player.closeInventory(); }
    }
}
