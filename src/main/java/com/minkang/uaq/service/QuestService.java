package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import com.minkang.uaq.model.Quest;
import java.util.*;
import org.bukkit.entity.Player;

public class QuestService {
    protected final UAQPlugin plugin;
    private final Map<String, Quest> pool = new HashMap<>();
    private final Map<java.util.UUID, Map<String, Integer>> progress = new HashMap<>();
    private final Map<java.util.UUID, Set<String>> assigned = new HashMap<>();

    public QuestService(UAQPlugin plugin){ this.plugin = plugin; }

    public Map<String, Quest> pool(){ return pool; }
    public void load(){}
    public void saveQuestCfg(){}
    public void assignDailies(Player p){ assigned.computeIfAbsent(p.getUniqueId(), k-> new HashSet<>()); }
    public Set<String> getAssigned(java.util.UUID u){ return assigned.getOrDefault(u, new HashSet<>()); }
    public boolean assignSpecific(Player p, String questId){
        assigned.computeIfAbsent(p.getUniqueId(), k-> new HashSet<>()).add(questId); return true;
    }
    public void addProgress(java.util.UUID u, String questId, int delta){
        int cur = getProgress(u, questId);
        progress.computeIfAbsent(u, k-> new HashMap<>()).put(questId, cur+delta);
    }
    public int getProgress(java.util.UUID u, String questId){
        return progress.getOrDefault(u, Collections.emptyMap()).getOrDefault(questId, 0);
    }
}
