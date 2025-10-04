package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import org.bukkit.entity.Player;
import java.util.*;

public class PassService {
    protected final UAQPlugin plugin;
    private final Map<java.util.UUID, Integer> level = new HashMap<>();
    private final Map<java.util.UUID, Integer> xp = new HashMap<>();
    private final java.util.Set<java.util.UUID> premium = new HashSet<>();

    public PassService(UAQPlugin plugin){ this.plugin = plugin; }

    public int getLevel(java.util.UUID u){ return level.getOrDefault(u, 0); }
    public int getXP(java.util.UUID u){ return xp.getOrDefault(u, 0); }
    public boolean isPremium(java.util.UUID u){ return premium.contains(u); }
    public void addXP(java.util.UUID u, int amount){ xp.put(u, getXP(u)+amount); }
    public void checkLevelUp(Player p){}
}
