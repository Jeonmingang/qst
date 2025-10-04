package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import org.bukkit.entity.Player;

public class CouponService {
    protected final UAQPlugin plugin;
    public CouponService(UAQPlugin plugin){ this.plugin = plugin; }
    public boolean redeem(Player p, String code){ return false; }
}
