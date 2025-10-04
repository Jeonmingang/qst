
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.configuration.ConfigurationSection; import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File; import java.io.IOException; import java.util.*;
public class PassService {
    public static class Level { public int need; public String freeSet; public String premiumSet; }
    private final UAQPlugin plugin; private final Map<Integer,Level> levels=new HashMap<>();
    private final File file; private final YamlConfiguration data;
    public PassService(UAQPlugin plugin){ this.plugin=plugin; this.file=new File(plugin.getDataFolder(),"pass.yml"); this.data=YamlConfiguration.loadConfiguration(file); reloadLevels(); }
    public void reloadLevels(){ levels.clear(); ConfigurationSection s=plugin.getConfig().getConfigurationSection("pass.levels"); if(s==null) return;
        for(String k: s.getKeys(false)){ try{ int lv=Integer.parseInt(k); ConfigurationSection c=s.getConfigurationSection(k); Level L=new Level(); L.need=c.getInt("xp", 100); L.freeSet=c.getString("free",""); L.premiumSet=c.getString("premium",""); levels.put(lv, L);}catch(Exception ignored){} } }
    private String P(java.util.UUID u, String k){ return "players."+u+"."+k; }
    public int getXP(java.util.UUID u){ return data.getInt(P(u,"xp"),0); }
    public int addXP(java.util.UUID u, int amt){ int v=Math.max(0, getXP(u)+Math.max(0,amt)); data.set(P(u,"xp"), v); save(); return v; }
    public int getLevel(java.util.UUID u){ return data.getInt(P(u,"lv"),1); }
    public void setLevel(java.util.UUID u, int lv){ data.set(P(u,"lv"), lv); save(); }
    public boolean isPremium(java.util.UUID u){ return data.getBoolean(P(u,"premium"), false); }
    public void setPremium(java.util.UUID u, boolean v){ data.set(P(u,"premium"), v); save(); }
    private void save(){ try{ data.save(file);}catch(IOException ignored){} }
    public void checkLevelUp(org.bukkit.entity.Player p){
        int xp=getXP(p.getUniqueId()); int lv=getLevel(p.getUniqueId());
        while(levels.containsKey(lv) && xp>=levels.get(lv).need){
            Level L=levels.get(lv);
            String set = isPremium(p.getUniqueId()) && L.premiumSet!=null && !L.premiumSet.isEmpty()? L.premiumSet : L.freeSet;
            if(set!=null && !set.isEmpty()) plugin.rewards().applySet(p, set);
            lv++; setLevel(p.getUniqueId(), lv);
            p.sendMessage(com.minkang.uaq.util.Text.color("&6패스 레벨업! Lv."+ (lv-1) +" → Lv."+lv));
        }
    }
}
