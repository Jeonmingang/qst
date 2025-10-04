package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import java.util.*;

public class DexService {
    protected final UAQPlugin plugin;
    private final Map<java.util.UUID, Integer> percent = new HashMap<>();
    private final Map<java.util.UUID, java.util.Set<Integer>> claimed = new HashMap<>();
    private final Map<Integer, String> rewardMap = new HashMap<>();

    public DexService(UAQPlugin plugin){ this.plugin = plugin; }

    public int getPercent(java.util.UUID u){ return percent.getOrDefault(u, 0); }
    public void setPercent(java.util.UUID u, int v){ percent.put(u, v); }
    public boolean newlyAchieved(java.util.UUID u, int v){ return true; }
    public String rewardSetFor(Integer idx){ return rewardMap.get(idx); }
    public void addClaimed(java.util.UUID u, Integer idx){
        claimed.computeIfAbsent(u, k-> new HashSet<>()).add(idx);
    }
}
