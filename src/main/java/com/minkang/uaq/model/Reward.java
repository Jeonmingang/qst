package com.minkang.uaq.model;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class Reward {
    public RewardType type = RewardType.MONEY;
    public double amount = 0;
    public String command;
    public ItemStack item;

    public static Reward from(ConfigurationSection sec){
        Reward r = new Reward();
        if (sec == null) return r;
        r.type = RewardType.valueOf(sec.getString("type","MONEY").toUpperCase());
        switch (r.type){
            case MONEY: r.amount = sec.getDouble("amount", 0); break;
            case COMMAND: r.command = sec.getString("command", ""); break;
            case ITEM: r.item = sec.getItemStack("item"); break;
            case PROTECT: r.amount = sec.getDouble("amount", 1); break;
            case POINTS: r.amount = sec.getDouble("amount", 1); break;
            case PASS_XP: r.amount = sec.getDouble("amount", 1); break;
        }
        return r;
    }
}
