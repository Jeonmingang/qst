package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import com.minkang.uaq.model.Reward;
import com.minkang.uaq.model.RewardSet;
import com.minkang.uaq.model.RewardType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RewardService {
    private final UAQPlugin plugin;
    private final Map<String, RewardSet> sets = new LinkedHashMap<>();
    private File rewardsFile;
    private YamlConfiguration rewardsCfg;

    public RewardService(UAQPlugin plugin){
        this.plugin = plugin;
        load();
    }

    public void load(){
        rewardsFile = new File(plugin.getDataFolder(), "rewards.yml");
        rewardsCfg = YamlConfiguration.loadConfiguration(rewardsFile);
        sets.clear();
        if (rewardsCfg.isConfigurationSection("rewardSets")){
            ConfigurationSection sec = rewardsCfg.getConfigurationSection("rewardSets");
            for (String id : sec.getKeys(false)){
                sets.put(id, RewardSet.from(id, sec.getConfigurationSection(id)));
            }
        }
    }

    public void save(){
        try{
            YamlConfiguration out = new YamlConfiguration();
            for (Map.Entry<String, RewardSet> e : sets.entrySet()){
                String path = "rewardSets." + e.getKey();
                RewardSet rs = e.getValue();
                out.set(path + ".name", rs.name);
                List<Map<String, Object>> list = new ArrayList<>();
                for (Reward r : rs.rewards){
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("type", r.type.name());
                    switch (r.type){
                        case MONEY: m.put("amount", r.amount); break;
                        case COMMAND: m.put("command", r.command); break;
                        case ITEM: m.put("item", r.item); break;
                        case PROTECT: m.put("amount", r.amount); break;
                        case POINTS: m.put("amount", r.amount); break;
                        case PASS_XP: m.put("amount", r.amount); break;
                    }
                    list.add(m);
                }
                out.set(path + ".rewards", list);
            }
            out.save(rewardsFile);
            rewardsCfg = out;
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public Collection<RewardSet> allSets(){ return sets.values(); }

    public RewardSet ensure(String id){
        return sets.computeIfAbsent(id, k -> {
            RewardSet rs = new RewardSet();
            rs.id = k;
            rs.name = "Set " + k;
            return rs;
        });
    }

    public void delete(String id){ sets.remove(id); save(); }

    public void give(Player p, Reward r){
        if (p == null || r == null) return;
        switch (r.type){
            case MONEY:
                // require Economy hook externally
                p.sendMessage("§6돈 §e+" + (int)r.amount);
                break;
            case COMMAND:
                if (r.command != null){
                    String cmd = r.command.replace("{player}", p.getName());
                    p.getServer().dispatchCommand(p.getServer().getConsoleSender(), cmd);
                }
                break;
            case ITEM:
                if (r.item != null) p.getInventory().addItem(r.item.clone());
                break;
            case PROTECT:
            case POINTS:
            case PASS_XP:
                // Not implemented in minimal build
                break;
        }
    }
}
