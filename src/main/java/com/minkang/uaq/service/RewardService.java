package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import com.minkang.uaq.model.RewardSet;
import com.minkang.uaq.model.Reward;
import org.bukkit.entity.Player;
import java.util.*;

public class RewardService {
    protected final UAQPlugin plugin;
    private final Map<String, RewardSet> sets = new HashMap<>();

    public RewardService(UAQPlugin plugin){ this.plugin = plugin; }

    public RewardSet get(String key){ return sets.get(key); }
    public java.util.Collection<RewardSet> allSets(){ return sets.values(); }
    public RewardSet ensure(String key){
        return sets.computeIfAbsent(key, k -> new RewardSet(k, k));
    }
    public void delete(String key){ sets.remove(key); }
    public void save(){}
    public void load(){}
    public void applySet(Player p, String key){
        RewardSet rs = sets.get(key);
        if (rs == null) return;
        for (Reward r : rs.rewards){ /* no-op stub */ }
    }
}
