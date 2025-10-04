
package com.minkang.uaq.gui;
import org.bukkit.entity.Player; import org.bukkit.event.*; import org.bukkit.event.inventory.*; import java.util.*;
public class MenuListener implements Listener {
    private static final Map<java.util.UUID, UMenu> open=new HashMap<>();
    public static void open(Player p, UMenu menu){ open.put(p.getUniqueId(), menu); p.openInventory(menu.getInventory()); }
    @EventHandler public void click(InventoryClickEvent e){ if(!(e.getWhoClicked() instanceof Player)) return; Player p=(Player)e.getWhoClicked(); UMenu m=open.get(p.getUniqueId());
        if(m==null) return; if(e.getView().getTopInventory().equals(m.getInventory())){ e.setCancelled(true); m.onClick(e);}}
    @EventHandler public void close(InventoryCloseEvent e){ open.remove(e.getPlayer().getUniqueId()); }
}
