
package com.minkang.uaq.model;
import org.bukkit.configuration.ConfigurationSection; import org.bukkit.inventory.ItemStack;
public class Reward { public com.minkang.uaq.model.RewardType type; public double amount; public String command; public ItemStack item;
    public static Reward from(ConfigurationSection sec){ Reward r=new Reward(); r.type=com.minkang.uaq.model.RewardType.valueOf(sec.getString("type","MONEY").toUpperCase());
        switch(r.type){ case MONEY: r.amount=sec.getDouble("amount",0); break; case COMMAND: r.command=sec.getString("command",""); break; case ITEM: r.item=sec.getItemStack("item"); break; } return r; } }
