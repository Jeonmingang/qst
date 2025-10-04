
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.Quest; import org.bukkit.Bukkit; import org.bukkit.boss.*; import org.bukkit.entity.Player; import java.util.*;
public class TrackService implements org.bukkit.event.Listener {
    private final UAQPlugin plugin; private final Map<java.util.UUID,String> pinned=new HashMap<>(); private final Map<java.util.UUID, BossBar> bars=new HashMap<>();
    public TrackService(UAQPlugin plugin){ this.plugin=plugin; }
    public void pin(Player p, String questId){ if(questId==null){ unpin(p); return; } pinned.put(p.getUniqueId(), questId); ensureBar(p); update(p); }
    public void unpin(Player p){ pinned.remove(p.getUniqueId()); BossBar b=bars.remove(p.getUniqueId()); if(b!=null) b.removeAll(); }
    public String getPinned(Player p){ return pinned.get(p.getUniqueId()); }
    public void update(Player p){
        String qid = pinned.get(p.getUniqueId()); if(qid==null) return;
        Quest q = plugin.quests().pool().get(qid); if(q==null){ unpin(p); return; }
        int prog = plugin.quests().getProgress(p.getUniqueId(), qid);
        double perc = Math.max(0, Math.min(1.0, prog/(double)q.goal));
        BossBar bar = ensureBar(p);
        bar.setTitle("§e"+q.name+" §7"+prog+"/"+q.goal);
        bar.setProgress(perc);
        bar.setVisible(true);
    }
    private BossBar ensureBar(Player p){
        BossBar bar = bars.get(p.getUniqueId());
        if(bar==null){ bar=Bukkit.createBossBar("UAQ", BarColor.GREEN, BarStyle.SEGMENTED_12); bar.addPlayer(p); bars.put(p.getUniqueId(), bar); }
        return bar;
    }
    @org.bukkit.event.EventHandler public void onQuit(org.bukkit.event.player.PlayerQuitEvent e){ unpin(e.getPlayer()); }
}
