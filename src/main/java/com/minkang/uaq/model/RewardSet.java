
package com.minkang.uaq.model;
import org.bukkit.configuration.ConfigurationSection; import java.util.*;
public class RewardSet { public String id,name; public java.util.List<com.minkang.uaq.model.Reward> rewards=new java.util.ArrayList<>();
    public static RewardSet from(String id, ConfigurationSection sec){ RewardSet rs=new RewardSet(); rs.id=id; rs.name=sec.getString("name", id);
        if(sec.isList("rewards")){ int idx=0; for(Object o: sec.getList("rewards")){ ConfigurationSection s=sec.getConfigurationSection("rewards."+idx); if(s!=null) rs.rewards.add(com.minkang.uaq.model.Reward.from(s)); idx++; } } return rs; } }
