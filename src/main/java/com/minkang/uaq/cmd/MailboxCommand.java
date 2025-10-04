
package com.minkang.uaq.cmd;
import com.minkang.uaq.gui.MailboxMenu; import com.minkang.uaq.gui.MenuListener;
import org.bukkit.command.*; import org.bukkit.entity.Player;
public class MailboxCommand implements CommandExecutor {
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if(!(s instanceof Player)){ s.sendMessage("플레이어만 사용 가능합니다."); return true; }
        Player p=(Player)s; MenuListener.open(p, new MailboxMenu(p)); return true;
    }
}
