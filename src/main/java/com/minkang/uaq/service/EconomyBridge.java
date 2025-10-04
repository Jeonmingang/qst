package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import org.bukkit.entity.Player;

public class EconomyBridge {
    protected final UAQPlugin plugin;
    public EconomyBridge(UAQPlugin plugin){ this.plugin = plugin; }
    public boolean has(Player p, double amount){ return true; }
    public boolean withdraw(Player p, double amount){ return true; }
    public void deposit(Player p, double amount){}
}
