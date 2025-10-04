package com.minkang.uaq.model;

public class Reward {
    public String type;
    public String value;
    public int amount;

    public Reward() {}
    public Reward(String type, String value, int amount){
        this.type = type;
        this.value = value;
        this.amount = amount;
    }
}
