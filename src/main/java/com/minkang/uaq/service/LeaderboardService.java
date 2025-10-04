package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import java.util.*;

public class LeaderboardService {
    protected final UAQPlugin plugin;
    public LeaderboardService(UAQPlugin plugin){ this.plugin = plugin; }
    public java.util.List<String> top(String board, int n){ return new ArrayList<>(); }
    public void resetWeekly(){}
}
