package com.minkang.uaq.model;

import java.util.ArrayList;
import java.util.List;

public class RewardSet {
    public String id;
    public String name;
    // Code accesses this field directly, so keep it public
    public final List<Reward> rewards = new ArrayList<>();

    public RewardSet(){}

    public RewardSet(String id, String name){
        this.id = id;
        this.name = name;
    }
}
