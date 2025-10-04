package com.minkang.uaq.util;

import org.bukkit.ChatColor;

public class Text {
    public static String color(String s){
        if (s == null) return null;
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static String strip(String s){
        if (s == null) return null;
        return ChatColor.stripColor(color(s));
    }
}
