package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import org.bukkit.entity.Player;
import java.time.LocalDate;
import java.util.*;

public class AttendanceService {
    protected final UAQPlugin plugin;
    private final Map<UUID, Set<LocalDate>> dateMap = new HashMap<>();
    private final Map<UUID, Integer> points = new HashMap<>();
    private final Map<UUID, Integer> streaks = new HashMap<>();
    private final Map<UUID, Integer> protector = new HashMap<>();

    public AttendanceService(UAQPlugin plugin){ this.plugin = plugin; }

    public int getStreak(UUID u){ return streaks.getOrDefault(u,0); }
    public boolean canClaim(UUID u){ return true; }
    public boolean claim(UUID u){ return true; }
    public int getPoints(UUID u){ return points.getOrDefault(u,0); }
    public Map<UUID, Set<LocalDate>> dates(){ return dateMap; }
    public Set<LocalDate> getDates(UUID u){ return dateMap.getOrDefault(u, new HashSet<>()); }
    public int getProtector(UUID u){ return protector.getOrDefault(u,0); }
    public boolean buyProtectorWithMoney(Player p, double amt){ return true; }
    public boolean buyProtectorWithPoints(UUID u, int cost){ return true; }
    public void addProtector(UUID u, int v){ protector.put(u, getProtector(u)+v); }
    public void save(){}
}
