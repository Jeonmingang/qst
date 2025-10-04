
package com.minkang.uaq.cmd;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.gui.MenuListener; import com.minkang.uaq.gui.QuestMenu; import org.bukkit.command.*; import org.bukkit.entity.Player; import java.util.*;
public class QuestCommand implements CommandExecutor, TabCompleter {
    @Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length==0){ if(!(sender instanceof Player)){ sender.sendMessage("플레이어만 사용 가능합니다."); return true; } Player p=(Player)sender; UAQPlugin.get().quests().assignDailies(p); MenuListener.open(p,new QuestMenu(p)); return true; }
        return true;
    }
    @Override public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args){ return java.util.Collections.emptyList(); }
}
