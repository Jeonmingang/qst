package com.minkang.uaq.model;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RewardSet {
    public String id, name;
    public List<Reward> rewards = new ArrayList<>();

    public static RewardSet from(String id, ConfigurationSection sec){
        RewardSet rs = new RewardSet();
        rs.id = id;
        rs.name = sec != null ? sec.getString("name", id) : id;
        if (sec != null && sec.isList("rewards")){
            for (Object o : sec.getList("rewards")){
                if (o instanceof ConfigurationSection){
                    rs.rewards.add(Reward.from((ConfigurationSection)o));
                }
            }
        }
        return rs;
    }
}
