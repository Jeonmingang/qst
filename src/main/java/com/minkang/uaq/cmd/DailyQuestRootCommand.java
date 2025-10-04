
package com.minkang.uaq.cmd;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.gui.MenuListener; import com.minkang.uaq.gui.QuestMenu; import com.minkang.uaq.gui.DexRewardConfigMenu; import com.minkang.uaq.gui.DexRewardClaimMenu; import com.minkang.uaq.util.Text;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player; import java.util.*;
public class DailyQuestRootCommand implements CommandExecutor {
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if(a.length==0 || a[0].equalsIgnoreCase("열기")){ if(!(s instanceof Player)){ s.sendMessage("플레이어만 사용 가능합니다."); return true; } MenuListener.open((Player)s, new QuestMenu((Player)s)); return true; }
        if(a[0].equalsIgnoreCase("도움말")){ s.sendMessage(Text.color("&e/일일퀘스트 열기 &7- 퀘스트 GUI")); s.sendMessage(Text.color("&e/일일퀘스트 npc연결 <ID|UUID> <questId>")); s.sendMessage(Text.color("&e/일일퀘스트 npc해제 <ID|UUID>")); s.sendMessage(Text.color("&e/일일퀘스트 도감보상 &7- 내 도감 보상 수령")); s.sendMessage(Text.color("&e/일일퀘스트 도감보상설정 &7- 관리자 도감 보상 GUI")); s.sendMessage(Text.color("&e/일일퀘스트 도감갱신 <퍼센트> &7- (브리지) 내 진행 갱신")); return true; }
        if(a[0].equalsIgnoreCase("npc연결")){ if(a.length<3){ s.sendMessage(Text.color("&c사용법: /일일퀘스트 npc연결 <ID|UUID> <questId>")); return true; } Bukkit.dispatchCommand(s, "관리 npc연결 "+a[1]+" quest "+a[2]); return true; }
        if(a[0].equalsIgnoreCase("npc해제")){ if(a.length<2){ s.sendMessage(Text.color("&c사용법: /일일퀘스트 npc해제 <ID|UUID>")); return true; } Bukkit.dispatchCommand(s, "관리 npc해제 "+a[1]); return true; }
        // 도감 관련 하위명령 제거됨
        // 도감 관련 하위명령 제거됨
        // 도감 관련 하위명령 제거됨
        s.sendMessage(Text.color("&c알 수 없는 하위명령입니다. /일일퀘스트 도움말"));
        return true;
    }
}
