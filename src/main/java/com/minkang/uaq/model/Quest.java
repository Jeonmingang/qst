package com.minkang.uaq.model;

import org.bukkit.Material;

public class Quest {
    // Public fields because some parts of the code reference fields directly (q.description, q.material, q.rewardSet)
    public String description;
    public Material material;
    public RewardSet rewardSet;

    public String id;
    public String name;

    public Quest(String id, String name, String description, Material material, RewardSet rewardSet){
        this.id = id;
        this.name = name;
        this.description = description;
        this.material = material;
        this.rewardSet = rewardSet;
    }

    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getDescription(){ return description; }
    public Material getMaterial(){ return material; }
    public RewardSet getRewardSet(){ return rewardSet; }
}
