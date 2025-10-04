
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.Bukkit; import org.bukkit.entity.Player; import org.bukkit.scoreboard.*;
public class ScoreboardService {
    private final UAQPlugin plugin;
    public ScoreboardService(UAQPlugin plugin){ this.plugin=plugin; }
    public void update(Player p){
        if(!plugin.getConfig().getBoolean("scoreboard.enabled", false)) return;
        ScoreboardManager m=Bukkit.getScoreboardManager(); if(m==null) return;
        Scoreboard b=m.getNewScoreboard();
        Objective o=b.registerNewObjective("uaq","dummy","§6UAQ");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        int line=7;
        o.getScore("§e연속: §f"+plugin.attendance().getStreak(p.getUniqueId())).setScore(line--);
        o.getScore("§b보호권: §f"+plugin.attendance().getProtector(p.getUniqueId())).setScore(line--);
        o.getScore("§d포인트: §f"+plugin.attendance().getPoints(p.getUniqueId())).setScore(line--);
        String pin = plugin.getTracker().getPinned(p);
        if(pin!=null){
            com.minkang.uaq.model.Quest q=plugin.quests().pool().get(pin);
            if(q!=null){
                int prog=plugin.quests().getProgress(p.getUniqueId(), pin);
                o.getScore("§7퀘: §f"+q.name).setScore(line--);
                o.getScore("§7진행: §f"+prog+"/"+q.goal).setScore(line--);
            }
        }
        o.getScore("§6패스Lv: §f"+plugin.pass().getLevel(p.getUniqueId())).setScore(line--);
        p.setScoreboard(b);
    }
}
