
package com.minkang.uaq.cmd;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public class GiftPointsCommand implements CommandExecutor {
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if(!(s instanceof Player)){ s.sendMessage("플레이어만 사용 가능합니다."); return true; }
        Player from=(Player)s;
        if(a.length<2){ from.sendMessage("§c사용법: /포인트보내 <닉네임> <수량>"); return true; }
        Player to = Bukkit.getPlayerExact(a[0]); if(to==null){ from.sendMessage("§c대상 플레이어가 접속중이 아닙니다."); return true; }
        int amt=1; try{ amt=Integer.parseInt(a[1]); } catch(Exception e){ from.sendMessage("§c수량은 숫자여야 합니다."); return true; }
        if(amt<=0){ from.sendMessage("§c수량은 1 이상이어야 합니다."); return true; }
        int have = com.minkang.uaq.UAQPlugin.get().attendance().getPoints(from.getUniqueId());
        if(have<amt){ from.sendMessage("§c포인트가 부족합니다."); return true; }
        double taxRate = com.minkang.uaq.UAQPlugin.get().getConfig().getDouble("gift.taxRate", 0.0);
        int send = (int)Math.max(0, Math.floor(amt * (1.0 - taxRate)));
        com.minkang.uaq.UAQPlugin.get().attendance().spendPoints(from.getUniqueId(), amt);
        com.minkang.uaq.UAQPlugin.get().attendance().addPoints(to.getUniqueId(), send);
        from.sendMessage("§d전송 완료: -" + amt + "P (세후 +" + send + "P to "+to.getName()+")");
        to.sendMessage("§d포인트 수령: +" + send + "P (from "+from.getName()+")");
        return true;
    }
}
