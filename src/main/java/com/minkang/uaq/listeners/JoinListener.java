
package com.minkang.uaq.listeners;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.gui.MenuListener; import com.minkang.uaq.gui.QuestMenu; import com.minkang.uaq.util.Text;
import org.bukkit.entity.Player; import org.bukkit.event.EventHandler; import org.bukkit.event.Listener; import org.bukkit.event.player.PlayerJoinEvent;
public class JoinListener implements Listener {
    private final UAQPlugin plugin;
    public JoinListener(UAQPlugin plugin){ this.plugin=plugin; }
    @EventHandler public void onJoin(PlayerJoinEvent e){
        Player p=e.getPlayer();
        // optional auto assign dailies
        plugin.quests().assignDailies(p);
        // optional auto-claim attendance
        if(plugin.getConfig().getBoolean("attendance.autoClaimOnJoin", false)){
            if(plugin.attendance().canClaim(p.getUniqueId())){
                int st=plugin.attendance().claim(p.getUniqueId());
                p.sendMessage(Text.color("&a자동 출석 처리! 연속: &e"+st));
                int cycle=plugin.getConfig().getInt("attendance.cycleDays",7); int day=(st-1)%cycle+1; String setId=plugin.getConfig().getString("attendance.dailyRewards."+day,"daily_"+day);
                plugin.rewards().applySet(p, setId);
            }
        } else {
            if(plugin.attendance().canClaim(p.getUniqueId())){
                p.sendTitle(Text.color("&e출석 보상이 준비됨"), Text.color("&7/출석 을 열어 받아주세요"), 10, 60, 10);
            }
        }
        plugin.getScoreboardService().update(p);
    }
}
