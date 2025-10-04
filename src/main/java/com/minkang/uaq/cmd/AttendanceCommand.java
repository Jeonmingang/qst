package com.minkang.uaq.cmd;

import com.minkang.uaq.gui.AttendanceMenu;
import com.minkang.uaq.gui.MenuListener;
import com.minkang.uaq.gui.PointShopMenu;
import com.minkang.uaq.gui.PassMenu;
import com.minkang.uaq.gui.SettingsMenu;
import com.minkang.uaq.gui.CalendarMenu;
import com.minkang.uaq.gui.NotifyMenu;
import com.minkang.uaq.gui.MailboxMenu;
import com.minkang.uaq.util.Text;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AttendanceCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0){
            MenuListener.open(p, new AttendanceMenu(p));
            return true;
        }
        String sub = args[0].toLowerCase(Locale.ROOT);

        switch (sub) {
            case "상점":
            case "shop":
                MenuListener.open(p, new PointShopMenu(p));
                p.sendMessage(Text.color("&d포인트 상점을 열었습니다."));
                return true;
            case "패스":
            case "pass":
                MenuListener.open(p, new PassMenu(p));
                p.sendMessage(Text.color("&d시즌 패스를 열었습니다."));
                return true;
            case "설정":
            case "settings":
                MenuListener.open(p, new SettingsMenu(p));
                p.sendMessage(Text.color("&d개인 설정을 열었습니다."));
                return true;
            case "달력":
            case "calendar":
                MenuListener.open(p, new CalendarMenu(p));
                p.sendMessage(Text.color("&d달력을 열었습니다."));
                return true;
            case "알림":
            case "notify":
                MenuListener.open(p, new NotifyMenu(p));
                p.sendMessage(Text.color("&d알림 로그를 열었습니다."));
                return true;
            case "우편함":
            case "mail":
            case "mailbox":
                MenuListener.open(p, new MailboxMenu(p));
                p.sendMessage(Text.color("&d우편함을 열었습니다."));
                return true;
            default:
                p.sendMessage(Text.color("&c알 수 없는 하위명령입니다. &7/출석 [상점|패스|설정|달력|알림|우편함]"));
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args){
        if(args.length == 1){
            return Arrays.asList("상점","패스","설정","달력","알림","우편함");
        }
        return Collections.emptyList();
    }
}