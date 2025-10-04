
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.*; import com.minkang.uaq.util.Text;
import net.md_5.bungee.api.ChatMessageType; import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*; import org.bukkit.configuration.ConfigurationSection; import org.bukkit.configuration.file.YamlConfiguration; import org.bukkit.entity.Player; import org.bukkit.event.*; import org.bukkit.event.block.BlockBreakEvent;
import java.io.File; import java.io.IOException; import java.time.ZoneId; import java.util.*;
public class QuestService implements Listener {
    private final UAQPlugin plugin; private File questFile; private YamlConfiguration questCfg; private File playerFile; private YamlConfiguration playerCfg;
    private final Map<String,Quest> questPool=new LinkedHashMap<>(); private final Map<java.util.UUID, Map<String,Integer>> progresses=new HashMap<>(); private final Map<java.util.UUID, java.util.Set<String>> assigned=new HashMap<>();
    public QuestService(UAQPlugin plugin){ this.plugin=plugin; load(); }
    public void load(){ questFile=new File(plugin.getDataFolder(),"quests.yml"); questCfg=YamlConfiguration.loadConfiguration(questFile);
        playerFile=new File(plugin.getDataFolder(),"quest-players.yml"); playerCfg=YamlConfiguration.loadConfiguration(playerFile); questPool.clear();
        if(questCfg.isConfigurationSection("quests")) for(String id: questCfg.getConfigurationSection("quests").getKeys(false)){ ConfigurationSection s=questCfg.getConfigurationSection("quests."+id);
            Quest q=new Quest(); q.id=id; q.name=s.getString("name", id); q.description=s.getString("description",""); q.type=QuestType.valueOf(s.getString("type","GENERIC_COUNTER").toUpperCase());
            q.material=s.getString("material","STONE"); q.goal=s.getInt("goal",1); q.daily=s.getBoolean("daily",true); q.rewardSet=s.getString("rewardSet","daily_1"); questPool.put(id,q);} }
    public void savePlayers(){ try{ for(java.util.UUID u: assigned.keySet()) playerCfg.set("players."+u+".assigned", new java.util.ArrayList<>(assigned.get(u)));
        for(java.util.UUID u: progresses.keySet()) for(Map.Entry<String,Integer> e: progresses.get(u).entrySet()) playerCfg.set("players."+u+".progress."+e.getKey(), e.getValue()); playerCfg.save(playerFile);} catch(IOException e){ e.printStackTrace(); } }
    public void saveQuestCfg(){ try{ YamlConfiguration out=new YamlConfiguration(); for(Map.Entry<String,Quest> e: questPool.entrySet()){ String p="quests."+e.getKey(); Quest q=e.getValue();
            out.set(p+".name", q.name); out.set(p+".description", q.description); out.set(p+".type", q.type.name()); out.set(p+".material", q.material); out.set(p+".goal", q.goal); out.set(p+".daily", q.daily); out.set(p+".rewardSet", q.rewardSet); }
        out.save(questFile); questCfg=out; } catch(IOException ex){ ex.printStackTrace(); } }
    public java.util.Set<String> getAssigned(java.util.UUID u){ if(assigned.containsKey(u)) return assigned.get(u); java.util.Set<String> set=new java.util.HashSet<>(playerCfg.getStringList("players."+u+".assigned")); assigned.put(u,set); return set; }
    public int getProgress(java.util.UUID u, String qid){ Map<String,Integer> map=progresses.computeIfAbsent(u,k->new java.util.HashMap<>()); if(map.containsKey(qid)) return map.get(qid); int v=playerCfg.getInt("players."+u+".progress."+qid,0); map.put(qid,v); return v; }
    public void addProgress(java.util.UUID u, String qid, int amount){ Quest q=questPool.get(qid); if(q==null) return; Map<String,Integer> map=progresses.computeIfAbsent(u,k->new java.util.HashMap<>()); int cur=map.getOrDefault(qid,0); int next=Math.min(q.goal, cur+Math.max(1,amount)); map.put(qid,next);
        Player pp=Bukkit.getPlayer(u); if(pp!=null){ int percent=(int)Math.round((next*100.0)/q.goal); String bar=progressBar(percent, 12);
            pp.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(Text.color("&e"+q.name+" &7"+bar+" &b"+next+"/"+q.goal+" ("+percent+"%)")));
            pp.playSound(pp.getLocation(), org.bukkit.Sound.valueOf(plugin.getConfig().getString("ux.sounds.progress","UI_TOAST_IN")), 1f,1f); }
        if(next>=q.goal){ Player p=Bukkit.getPlayer(u); if(p!=null) p.sendMessage(Text.color(plugin.getConfig().getString("messages.questCompleted"))); }
        Player p2=Bukkit.getPlayer(u); if(p2!=null){ com.minkang.uaq.UAQPlugin.get().getTracker().update(p2); com.minkang.uaq.UAQPlugin.get().getScoreboardService().update(p2);} }
    private String progressBar(int percent, int width){ int filled=(int)Math.round(width*percent/100.0); StringBuilder sb=new StringBuilder("&a"); for(int i=0;i<filled;i++) sb.append("■"); sb.append("&7"); for(int i=filled;i<width;i++) sb.append("■"); return sb.toString(); }
    public void assignDailies(Player p){ if(!plugin.getConfig().getBoolean("quests.assignDailyOnJoin", true)) return; java.util.Set<String> set=getAssigned(p.getUniqueId());
        if(set.size()>=plugin.getConfig().getInt("quests.maxDailyQuests",3)) return; java.util.List<String> ids=new java.util.ArrayList<>(questPool.keySet()); java.util.Collections.shuffle(ids);
        for(String id: ids){ if(set.size()>=plugin.getConfig().getInt("quests.maxDailyQuests",3)) break; if(!set.contains(id) && questPool.get(id).daily) set.add(id);} p.sendMessage(Text.color(plugin.getConfig().getString("messages.questAssigned"))); }
    public Map<String,Quest> pool(){ return questPool; }
    public void assignSpecific(Player p, String questId){ if(questPool.containsKey(questId)) getAssigned(p.getUniqueId()).add(questId); }
    public boolean canReroll(Player p){ if(!plugin.getConfig().getBoolean("reroll.allow", true)) return false; String tz=plugin.getConfig().getString("timezone","Asia/Seoul"); String today=java.time.LocalDate.now(ZoneId.of(tz)).toString();
        int used=playerCfg.getInt("players."+p.getUniqueId()+".reroll."+today,0); return used<plugin.getConfig().getInt("reroll.maxPerDay",1); }
    public void markRerollUsed(Player p){ String tz=plugin.getConfig().getString("timezone","Asia/Seoul"); String today=java.time.LocalDate.now(ZoneId.of(tz)).toString();
        int used=playerCfg.getInt("players."+p.getUniqueId()+".reroll."+today,0); playerCfg.set("players."+p.getUniqueId()+".reroll."+today, used+1); try{ playerCfg.save(playerFile);}catch(Exception ignored){} }
    public boolean reroll(Player p, String oldQuestId){ java.util.Set<String> set=getAssigned(p.getUniqueId()); if(!set.contains(oldQuestId)) return false; if(!canReroll(p)) return false;
        double cost=plugin.getConfig().getDouble("reroll.costMoney",0); if(cost>0 && !plugin.economy().withdraw(p.getName(), cost)){ p.sendMessage(Text.color("&c리롤에 필요한 돈이 부족합니다.")); return false; }
        java.util.List<String> ids=new java.util.ArrayList<>(questPool.keySet()); java.util.Collections.shuffle(ids); for(String id: ids){ if(!set.contains(id) && questPool.get(id).daily){ set.remove(oldQuestId); set.add(id); markRerollUsed(p); return true; } } return false; }
    @EventHandler public void onBreak(BlockBreakEvent e){ Player p=e.getPlayer(); java.util.Set<String> set=getAssigned(p.getUniqueId());
        for(String id: set){ Quest q=questPool.get(id); if(q==null) continue; if(q.type==QuestType.BLOCK_BREAK){ try{ Material m=Material.valueOf(q.material.toUpperCase());
                    if(e.getBlock().getType()==m) addProgress(p.getUniqueId(), id, 1); } catch(IllegalArgumentException ignored){} } } }
}
