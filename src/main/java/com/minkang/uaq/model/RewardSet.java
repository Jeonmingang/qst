package com.minkang.uaq.model;

import java.util.ArrayList;
import java.util.List;

public class RewardSet {
    private final String key;
    private final List<String> rewards = new ArrayList<>();

    public RewardSet(String key){
        this.key = key;
    }
    public String getKey(){ return key; }
    public List<String> rewards(){ return rewards; }
}
