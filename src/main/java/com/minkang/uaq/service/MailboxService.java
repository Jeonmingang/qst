
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import java.io.File; import java.io.IOException; import java.util.*;
public class MailboxService {
    private final UAQPlugin plugin; private final File file; private final YamlConfiguration cfg;
    private final Map<java.util.UUID, java.util.List<ItemStack>> cache=new HashMap<>();
    public MailboxService(UAQPlugin plugin){ this.plugin=plugin; this.file=new File(plugin.getDataFolder(), "mailbox.yml"); this.cfg=YamlConfiguration.loadConfiguration(file); }
    @SuppressWarnings("unchecked")
    public java.util.List<ItemStack> get(java.util.UUID u){ if(cache.containsKey(u)) return cache.get(u); java.util.List<ItemStack> list=(java.util.List<ItemStack>)cfg.getList("boxes."+u, new java.util.ArrayList<>()); cache.put(u, list); return list; }
    public void add(java.util.UUID u, ItemStack it){ java.util.List<ItemStack> list=get(u); list.add(it); save(u, list); }
    public int claimAll(org.bukkit.entity.Player p){ java.util.List<ItemStack> list=get(p.getUniqueId()); int delivered=0; java.util.Iterator<ItemStack> it=list.iterator();
        while(it.hasNext()){ ItemStack item=it.next(); java.util.HashMap<Integer, ItemStack> left=p.getInventory().addItem(item); if(left.isEmpty()){ it.remove(); delivered++; } else { // partially delivered, update remaining stack and stop
                ItemStack rem = left.values().iterator().next(); item.setAmount(rem.getAmount()); break; } }
        save(p.getUniqueId(), list); return delivered; }
    private void save(java.util.UUID u, java.util.List<ItemStack> list){ cfg.set("boxes."+u, list); try{ cfg.save(file); } catch(IOException ignored){} }
}
