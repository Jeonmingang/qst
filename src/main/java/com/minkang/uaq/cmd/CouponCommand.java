
package com.minkang.uaq.cmd;
import org.bukkit.command.*; import org.bukkit.entity.Player;
public class CouponCommand implements CommandExecutor {
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if(!(s instanceof Player)){ s.sendMessage("플레이어만 사용 가능합니다."); return true; }
        Player p=(Player)s; if(a.length<1){ p.sendMessage("§c사용법: /쿠폰 <코드>"); return true; }
        boolean ok = com.minkang.uaq.UAQPlugin.get().coupons().redeem(p, a[0]);
        p.sendMessage(ok? "§a쿠폰 적용 완료!" : "§c쿠폰이 없거나 이미 사용했습니다.");
        return true;
    }
}
