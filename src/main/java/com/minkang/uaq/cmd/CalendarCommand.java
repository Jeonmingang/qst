
package com.minkang.uaq.cmd;
import com.minkang.uaq.gui.MenuListener; import com.minkang.uaq.gui.CalendarMenu;
import org.bukkit.command.*; import org.bukkit.entity.Player;
public class CalendarCommand implements CommandExecutor {
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if(!(s instanceof Player)){ s.sendMessage("플레이어만 사용 가능합니다."); return true; }
        Player p=(Player)s; MenuListener.open(p, new CalendarMenu(p)); return true;
    }
}
