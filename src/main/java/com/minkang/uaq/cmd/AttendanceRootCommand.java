
package com.minkang.uaq.cmd;

import com.minkang.uaq.gui.AttendanceMenu;
import com.minkang.uaq.gui.MenuListener;
import com.minkang.uaq.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AttendanceRootCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        // default: open GUI
        if (a.length == 0 || a[0].equalsIgnoreCase("열기")) {
            if (!(s instanceof Player)) { s.sendMessage("플레이어만 사용 가능합니다."); return true; }
            Player p = (Player) s;
            MenuListener.open(p, new AttendanceMenu(p));
            return true;
        }

        // help
        if (a[0].equalsIgnoreCase("도움말")) {
            s.sendMessage(Text.color("&e/출석 열기 &7- 출석 GUI 열기"));
            s.sendMessage(Text.color("&e/출석 npc연결 <ID|UUID> &7- 출석 NPC 연동"));
            s.sendMessage(Text.color("&e/출석 npc해제 <ID|UUID> &7- 출석 NPC 연동 해제"));
            s.sendMessage(Text.color("&e/출석 보상설정 &7- 보상/세트 설정 GUI"));
            s.sendMessage(Text.color("&e/출석 상점 &7- 포인트 상점 열기"));
            s.sendMessage(Text.color("&e/출석 랭킹 &7- 랭킹 GUI 열기"));
            s.sendMessage(Text.color("&e/출석 패스 &7- 시즌 패스 GUI 열기"));
            s.sendMessage(Text.color("&e/출석 달력 &7- 월간 출석 달력 열기"));
            s.sendMessage(Text.color("&e/출석 알림 &7- 최근 보상 알림 GUI 열기"));
            s.sendMessage(Text.color("&e/출석 우편함 &7- 우편함 열기"));
            return true;
        }

        // NPC linkage (delegated to /관리 to reuse permission/logic)
        if (a[0].equalsIgnoreCase("npc연결")) {
            if (a.length < 2) { s.sendMessage(Text.color("&c사용법: /출석 npc연결 <ID|UUID>")); return true; }
            Bukkit.dispatchCommand(s, "관리 npc연결 " + a[1] + " attendance");
            return true;
        }
        if (a[0].equalsIgnoreCase("npc해제")) {
            if (a.length < 2) { s.sendMessage(Text.color("&c사용법: /출석 npc해제 <ID|UUID>")); return true; }
            Bukkit.dispatchCommand(s, "관리 npc해제 " + a[1]);
            return true;
        }
        if (a[0].equalsIgnoreCase("보상설정")) {
            if (!(s instanceof Player)) { s.sendMessage("플레이어만 사용 가능합니다."); return true; }
            Bukkit.dispatchCommand(s, "관리");
            return true;
        }

        // NEW: nested shortcuts
        if (a[0].equalsIgnoreCase("상점")) {
            Bukkit.dispatchCommand(s, "상점"); return true;
        }
        if (a[0].equalsIgnoreCase("랭킹")) {
            Bukkit.dispatchCommand(s, "랭킹"); return true;
        }
        if (a[0].equalsIgnoreCase("패스")) {
            Bukkit.dispatchCommand(s, "패스"); return true;
        }
        if (a[0].equalsIgnoreCase("달력")) {
            Bukkit.dispatchCommand(s, "달력"); return true;
        }
        if (a[0].equalsIgnoreCase("알림")) {
            Bukkit.dispatchCommand(s, "알림"); return true;
        }
        if (a[0].equalsIgnoreCase("우편함")) {
            Bukkit.dispatchCommand(s, "우편함"); return true;
        }

        s.sendMessage(Text.color("&c알 수 없는 하위명령입니다. /출석 도움말"));
        return true;
    }
}
