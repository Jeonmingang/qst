
package com.minkang.uaq.cmd;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public class ReferCommand implements CommandExecutor {
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if(!(s instanceof Player)){ s.sendMessage("플레이어만 사용 가능합니다."); return true; }
        Player p=(Player)s; if(a.length<1){ p.sendMessage("§c사용법: /추천인 <닉네임>"); return true; }
        Player ref = Bukkit.getPlayerExact(a[0]); if(ref==null){ p.sendMessage("§c대상 플레이어가 접속중이 아닙니다."); return true; }
        boolean ok = com.minkang.uaq.UAQPlugin.get().ref().setReferrer(p.getUniqueId(), ref.getUniqueId());
        p.sendMessage(ok? "§a추천인 등록 완료!" : "§c이미 추천인을 등록했거나 자기 자신입니다.");
        return true;
    }
}
