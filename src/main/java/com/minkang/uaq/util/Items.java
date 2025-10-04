package com.minkang.uaq.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static ItemStack button(Material m, String name, String... lore){
        ItemStack it = new ItemStack(m);
        ItemMeta im = it.getItemMeta();
        if (im != null) {
            im.setDisplayName(Text.color(name));
            List<String> l = new ArrayList<>();
            if (lore != null) {
                for (String s : lore) {
                    l.add(Text.color(s));
                }
            }
            im.setLore(l);
            it.setItemMeta(im);
        }
        return it;
    }
}