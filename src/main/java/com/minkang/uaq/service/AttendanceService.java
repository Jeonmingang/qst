
package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import com.minkang.uaq.util.Dates;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Daily attendance tracking + simple point/protector store.
 * Backed by YAML at plugins/UAQ/players.yml
 */
public class AttendanceService {

    public static class PlayerAttendance {
        public int streak;
        public String lastDate;
        public List<String> dates = new ArrayList<>();
        public int protector;
        public int points;
    }

    private final UAQPlugin plugin;
    private final Dates datesUtil;

    private File file;
    private YamlConfiguration data;

    // hot caches
    private final Map<UUID, Integer> streak = new HashMap<>();
    private final Map<UUID, String> lastDate = new HashMap<>();
    private final Map<UUID, List<String>> dateList = new HashMap<>();
    private final Map<UUID, Integer> protector = new HashMap<>();
    private final Map<UUID, Integer> points = new HashMap<>();

    public AttendanceService(UAQPlugin plugin) {
        this.plugin = plugin;
        this.datesUtil = new Dates(plugin.getConfig());
        load();
    }

    private String key(UUID u, String tail) {
        return "players." + u + "." + tail;
    }

    private void ensureFile() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), "players.yml");
        }
        if (data == null) {
            data = YamlConfiguration.loadConfiguration(file);
        }
    }

    private void load() {
        ensureFile();
    }

    public void save() {
        ensureFile();
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save attendance: " + e.getMessage());
        }
    }

    public int getStreak(UUID u) {
        Integer c = streak.get(u);
        if (c != null) return c;
        ensureFile();
        int s = data.getInt(key(u, "streak"), 0);
        streak.put(u, s);
        return s;
    }

    public String getLastDate(UUID u) {
        String ld = lastDate.get(u);
        if (ld != null) return ld;
        ensureFile();
        String v = data.getString(key(u, "lastDate"), "");
        if (v == null) v = "";
        lastDate.put(u, v);
        return v;
    }

    public List<String> getDates(UUID u) {
        List<String> list = dateList.get(u);
        if (list != null) return list;
        ensureFile();
        List<String> v = data.getStringList(key(u, "dates"));
        if (v == null) v = new ArrayList<>();
        dateList.put(u, v);
        return v;
    }

    public Dates dates() {
        return datesUtil;
    }

    public int getProtector(UUID u) {
        Integer v = protector.get(u);
        if (v != null) return v;
        ensureFile();
        int p = data.getInt(key(u, "protector"), 0);
        protector.put(u, p);
        return p;
    }

    public void addProtector(UUID u, int amount) {
        int v = Math.max(0, getProtector(u) + Math.max(1, amount));
        protector.put(u, v);
        data.set(key(u, "protector"), v);
        save();
    }

    public int getPoints(UUID u) {
        Integer v = points.get(u);
        if (v != null) return v;
        ensureFile();
        int p = data.getInt(key(u, "points"), 0);
        points.put(u, p);
        return p;
    }

    public void addPoints(UUID u, int amount) {
        int v = Math.max(0, getPoints(u) + amount);
        points.put(u, v);
        data.set(key(u, "points"), v);
        save();
    }

    public boolean spendPoints(UUID u, int amount) {
        int have = getPoints(u);
        if (amount <= 0) return true;
        if (have < amount) return false;
        int rem = have - amount;
        points.put(u, rem);
        data.set(key(u, "points"), rem);
        save();
        return true;
    }

    public boolean buyProtectorWithPoints(UUID u, int costPoints) {
        if (!spendPoints(u, costPoints)) return false;
        addProtector(u, 1);
        return true;
    }

    public boolean buyProtectorWithMoney(Player p, double cost) {
        if (!plugin.economy().withdraw(p.getName(), cost)) return false;
        addProtector(p.getUniqueId(), 1);
        return true;
    }

    public boolean canClaim(UUID u) {
        String last = getLastDate(u);
        String today = datesUtil.fmt(datesUtil.todayGameDate());
        return !today.equals(last);
    }

    /** Returns new streak after claim. */
    public int claim(UUID u) {
        LocalDate today = datesUtil.todayGameDate();
        String todayStr = datesUtil.fmt(today);

        List<String> list = new ArrayList<>(getDates(u));
        if (!list.contains(todayStr)) {
            list.add(todayStr);
        }
        dateList.put(u, list);
        data.set(key(u, "dates"), list);

        int st = getStreak(u);
        String last = getLastDate(u);
        LocalDate yesterday = today.minusDays(1);
        if (todayStr.equals(last)) {
            // already claimed today
        } else if (yesterday.equals(parseDate(last))) {
            st = st + 1;
        } else {
            int prot = getProtector(u);
            if (prot > 0) {
                prot -= 1;
                protector.put(u, prot);
                data.set(key(u, "protector"), prot);
            } else {
                st = 1;
            }
        }
        streak.put(u, st);
        data.set(key(u, "streak"), st);
        lastDate.put(u, todayStr);
        data.set(key(u, "lastDate"), todayStr);

        int per = plugin.getConfig().getInt("attendance.pointsPerClaim", 0);
        if (per > 0) {
            addPoints(u, per);
        }

        save();
        return st;
    }

    private LocalDate parseDate(String s) {
        try {
            if (s == null || s.isEmpty()) return null;
            return LocalDate.parse(s);
        } catch (Exception ignore) {
            return null;
        }
    }
}
