
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.entity.Player; import java.util.*;
public class PartyBonusService {
    private final UAQPlugin plugin; private final java.util.Deque<Long> claimTimes=new java.util.ArrayDeque<>(); private final Set<java.util.UUID> rewardedToday=new HashSet<>();
    public PartyBonusService(UAQPlugin plugin){ this.plugin=plugin; }
    public void onClaim(Player p){
        long now=System.currentTimeMillis();
        claimTimes.addLast(now);
        long window = plugin.getConfig().getLong("party.windowMs", 300000L);
        while(!claimTimes.isEmpty() && now-claimTimes.peekFirst() > window) claimTimes.removeFirst();
        int need = plugin.getConfig().getInt("party.threshold", 3);
        if(claimTimes.size()>=need && !rewardedToday.contains(p.getUniqueId())){
            rewardedToday.add(p.getUniqueId());
            int pts = plugin.getConfig().getInt("party.bonusPoints", 3);
            plugin.attendance().addPoints(p.getUniqueId(), pts);
            p.sendMessage(com.minkang.uaq.util.Text.color("&d파티 보너스! +" + pts + "P"));
        }
    }
    public void resetDaily(){ rewardedToday.clear(); }
}
