package com.minkang.uaq.model;

import org.bukkit.Material;

public class Quest {
    // Fields referenced directly in code
    public String id;
    public String name;
    public String description;
    public Material material;
    public RewardSet rewardSet;
    public int goal;

    public Quest() {}

    public Quest(String id, String name, String description, Material material, RewardSet rewardSet, int goal){
        this.id = id;
        this.name = name;
        this.description = description;
        this.material = material;
        this.rewardSet = rewardSet;
        this.goal = goal;
    }

    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getDescription(){ return description; }
    public Material getMaterial(){ return material; }
    public RewardSet getRewardSet(){ return rewardSet; }
    public int getGoal(){ return goal; }
}
