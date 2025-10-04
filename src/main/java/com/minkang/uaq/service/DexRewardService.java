
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.Bukkit; import org.bukkit.configuration.ConfigurationSection; import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File; import java.io.IOException; import java.util.*;
public class DexRewardService {
    private final UAQPlugin plugin; private final File dataFile; private final YamlConfiguration data;
    public DexRewardService(UAQPlugin plugin){ this.plugin=plugin; this.dataFile=new File(plugin.getDataFolder(),"dex.yml"); this.data=YamlConfiguration.loadConfiguration(dataFile); }
    private String P(java.util.UUID u, String k){ return "players."+u+"."+k; }
    public int getPercent(java.util.UUID u){ return Math.max(0, Math.min(100, data.getInt(P(u,"percent"), 0))); }
    public void setPercent(java.util.UUID u, int p){ int old = getPercent(u); int np = Math.max(0, Math.min(100, p)); data.set(P(u,"percent"), np); save(); if(np>old){ onProgress(u, np); } }
    public java.util.Set<Integer> claimed(java.util.UUID u){ java.util.List<Integer> list=data.getIntegerList(P(u,"claimed")); return new java.util.HashSet<>(list); }
    public void addClaimed(java.util.UUID u, int t){ java.util.Set<Integer> set=claimed(u); set.add(t); data.set(P(u,"claimed"), new java.util.ArrayList<>(set)); save(); }
    public java.util.SortedSet<Integer> thresholds(){ java.util.SortedSet<Integer> set=new java.util.TreeSet<>(); ConfigurationSection s=plugin.getConfig().getConfigurationSection("dex.thresholds"); if(s==null) return set;
        for(String k: s.getKeys(false)){ try{ set.add(Integer.parseInt(k)); }catch(Exception ignored){} } return set; }
    public String rewardSetFor(int percent){ return plugin.getConfig().getString("dex.thresholds."+percent, ""); }
    public java.util.List<Integer> newlyAchieved(java.util.UUID u, int newPercent){
        java.util.Set<Integer> done=claimed(u); java.util.List<Integer> res=new java.util.ArrayList<>();
        for(Integer t: thresholds()){ if(newPercent>=t && !done.contains(t)) res.add(t); } return res;
    }
    private void onProgress(java.util.UUID u, int newPercent){
        java.util.List<Integer> newly = newlyAchieved(u, newPercent);
        org.bukkit.OfflinePlayer op = Bukkit.getOfflinePlayer(u);
        for(Integer t: newly){
            String msg = "§6[축하] §f"+ (op!=null && op.getName()!=null? op.getName() : u.toString().substring(0,8)) +"§7 님이 도감을 §e"+t+"% §7달성했습니다! §e/도감 §7에서 보상 수령";
            Bukkit.broadcastMessage(msg);
        }
    }
    private void save(){ try{ data.save(dataFile);}catch(IOException ignored){} }
}
