package com.minkang.uaq.gui;

import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

class MenuRegistry {
    private static final Set<Inventory> TRACK = Collections.newSetFromMap(new WeakHashMap<>());

    static void track(Inventory inv){ if (inv != null) TRACK.add(inv); }
    static void untrack(Inventory inv){ if (inv != null) TRACK.remove(inv); }
    static boolean isTracked(Inventory inv){ return inv != null && TRACK.contains(inv); }
}
