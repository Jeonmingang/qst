package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import com.minkang.uaq.model.RewardSet;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RewardService {
    protected final UAQPlugin plugin;
    private final Map<String, RewardSet> sets = new HashMap<>();

    public RewardService(UAQPlugin plugin){
        this.plugin = plugin;
    }

    // Needed by menus
    public RewardSet get(String key){
        return sets.get(key);
    }

    // Minimal apply hook used across the codebase; no-op implementation (extend later)
    public void applySet(Player player, String key){
        // TODO: grant actual rewards; for now just a stub to compile
        RewardSet rs = sets.get(key);
        if (rs == null) return;
    }

    // Helper for wiring test data
    public void register(String key, RewardSet set){
        sets.put(key, set);
    }
}
