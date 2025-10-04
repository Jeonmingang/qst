
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.Dates; import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File; import java.io.IOException; import java.time.LocalDate; import java.util.*;
public class AttendanceService {
    public static class PlayerAttendance {
        public int streak;
        public String lastDate;
        public java.util.List<String> dates = new java.util.ArrayList<>();
        public int protector;
    }
    private final UAQPlugin plugin; private final Dates dates; private File file; private YamlConfiguration data; private final Map<UUID,Integer> streak=new HashMap<>(); private final Map<UUID,String> lastDate=new HashMap<>();
    public AttendanceService(UAQPlugin plugin){ this.plugin=plugin; this.dates=new Dates(plugin.getConfig()); load(); }
    private void load(){ file=new File(plugin.getDataFolder(),"players.yml"); data=YamlConfiguration.loadConfiguration(file); }
    
public void save(){ try{
    for(UUID u: streak.keySet()) data.set("players."+u+".streak", streak.get(u));
    for(UUID u: lastDate.keySet()) data.set("players."+u+".lastDate", lastDate.get(u));
    for(UUID u: protector.keySet()) data.set("players."+u+".protector", protector.get(u));
    for(UUID u: datesMap.keySet()) data.set("players."+u+".dates", datesMap.get(u));
    data.save(file);
} catch(IOException e){ e.printStackTrace(); } }
    public boolean canClaim(UUID u){ LocalDate today=dates.todayGameDate(); String t=dates.fmt(today); String last=getLastDate(u); return !t.equals(last); }
    
public int claim(UUID u){
    LocalDate today=dates.todayGameDate();
    String t=dates.fmt(today);
    String last=getLastDate(u);
    int st=getStreak(u);
    if(t.equals(last)) return st; // already
    LocalDate lastD = null;
    try{ lastD = (last==null||last.isEmpty())? null : LocalDate.parse(last); } catch(Exception ignored){}
    if(lastD!=null){
        long gap = java.time.temporal.ChronoUnit.DAYS.between(lastD, today);
        if(gap==1){ st+=1; }
        else if(gap==2 && getProtector(u)>0){ // one miss, consume protector
            protector.put(u, getProtector(u)-1);
            st+=1;
        } else {
            st=1;
        }
    } else {
        st=1;
    }
    streak.put(u,st);
    lastDate.put(u,t);
    java.util.List<String> list=getDates(u);
    if(list.size()>90) list = new java.util.ArrayList<>(list.subList(list.size()-90, list.size()));
    list.add(t);
    datesMap.put(u, list);
    return st;
}
    public int getStreak(UUID u){ if(streak.containsKey(u)) return streak.get(u); int st=data.getInt("players."+u+".streak",0); streak.put(u,st); return st; }
    public String getLastDate(UUID u){ if(lastDate.containsKey(u)) return lastDate.get(u); String last=data.getString("players."+u+".lastDate",""); lastDate.put(u, last==null?"":last); return lastDate.get(u); }
}

public int getProtector(java.util.UUID u){ if(protector.containsKey(u)) return protector.get(u); int v=data.getInt("players."+u+".protector",0); protector.put(u,v); return v; }
public void addProtector(java.util.UUID u, int amount){ int v=getProtector(u)+Math.max(1,amount); protector.put(u,v); }
public java.util.List<String> getDates(java.util.UUID u){ if(datesMap.containsKey(u)) return datesMap.get(u); java.util.List<String> list=data.getStringList("players."+u+".dates"); datesMap.put(u, list==null? new java.util.ArrayList<>() : list); return datesMap.get(u); }

public com.minkang.uaq.util.Dates dates(){ return dates; }

public int getPoints(java.util.UUID u){ if(points.containsKey(u)) return points.get(u); int v=data.getInt("players."+u+".points",0); points.put(u,v); return v; }
public void addPoints(java.util.UUID u, int amount){ int v=getPoints(u)+Math.max(1, amount); points.put(u, v); }
public boolean spendPoints(java.util.UUID u, int amount){ int v=getPoints(u); if(v<amount) return false; points.put(u, v-amount); return true; }
public boolean buyProtectorWithPoints(java.util.UUID u, int costPoints){ if(costPoints<=0) return false; if(!spendPoints(u, costPoints)) return false; addProtector(u,1); return true; }
public boolean buyProtectorWithMoney(org.bukkit.entity.Player p, double cost){ if(cost<=0) return false; if(!com.minkang.uaq.UAQPlugin.get().economy().withdraw(p.getName(), cost)) return false; addProtector(p.getUniqueId(),1); return true; }
