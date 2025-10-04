
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.configuration.file.YamlConfiguration; import java.io.File; import java.io.IOException; import java.util.*;
public class LeaderboardService {
    private final UAQPlugin plugin; private final File file; private final YamlConfiguration cfg;
    public LeaderboardService(UAQPlugin plugin){ this.plugin=plugin; this.file=new File(plugin.getDataFolder(),"leaderboard.yml"); this.cfg=YamlConfiguration.loadConfiguration(file); }
    public int addWeeklyPoints(java.util.UUID u, int amt){ int v=cfg.getInt("weekly."+u,0)+amt; cfg.set("weekly."+u,v); save(); return v; }
    public int addSeasonPoints(java.util.UUID u, int amt){ int v=cfg.getInt("season."+u,0)+amt; cfg.set("season."+u,v); save(); return v; }
    public int getWeekly(java.util.UUID u){ return cfg.getInt("weekly."+u,0); }
    public int getSeason(java.util.UUID u){ return cfg.getInt("season."+u,0); }
    public void resetWeekly(){ cfg.set("weekly", null); save(); }
    public void resetSeason(){ cfg.set("season", null); save(); }
    public java.util.List<java.util.Map.Entry<java.util.UUID,Integer>> top(String key, int n){
        java.util.Map<java.util.UUID,Integer> map=new java.util.HashMap<>();
        for(String k: cfg.getConfigurationSection(key)==null? java.util.Collections.<String>emptyList() : cfg.getConfigurationSection(key).getKeys(false)){
            try{ map.put(java.util.UUID.fromString(k), cfg.getInt(key+"."+k)); }catch(Exception ignored){}
        }
        java.util.List<java.util.Map.Entry<java.util.UUID,Integer>> list=new java.util.ArrayList<>(map.entrySet());
        list.sort((a,b)->Integer.compare(b.getValue(), a.getValue()));
        if(list.size()>n) return list.subList(0,n); return list;
    }
    private void save(){ try{ cfg.save(file);}catch(IOException ignored){} }
}
