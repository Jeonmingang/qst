
package com.minkang.uaq.cmd;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.gui.AdminMenu; import com.minkang.uaq.gui.MenuListener; import com.minkang.uaq.util.Text; import org.bukkit.command.*; import org.bukkit.entity.Player; import java.util.*;
public class AdminCommand implements CommandExecutor, TabCompleter {
    @Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length==0){ if(!(sender instanceof Player)){ sender.sendMessage("플레이어만 사용 가능합니다."); return true; } Player p=(Player)sender; if(!p.hasPermission("uaq.admin")){ p.sendMessage(Text.color("&c권한이 없습니다.")); return true; } MenuListener.open(p,new AdminMenu()); p.sendMessage(Text.color("&7도움말: /관리 도움말")); return true; }
        String sub=args[0];
        if(sub.equalsIgnoreCase("도움말") || sub.equalsIgnoreCase("help")){
            sender.sendMessage(Text.color("&6[UAQ] &f명령 도움말"));
            sender.sendMessage(Text.color("&e/출석 &7- 출석 GUI 열기"));
            sender.sendMessage(Text.color("&e/퀘스트 &7- 퀘스트 GUI 열기"));
            sender.sendMessage(Text.color("&e/관리 reload &7- 설정/리소스 리로드"));
            sender.sendMessage(Text.color("&e/관리 npc연결 <ID|UUID> attendance|quest <questId> &7- NPC 연동"));
            sender.sendMessage(Text.color("&e/관리 npc해제 <ID|UUID> &7- NPC 연동 해제"));
            sender.sendMessage(Text.color("&e/관리 진행 <player> <questId> <amount> &7- 픽셀몬 브리지"));
            return true;
        }
        if(sub.equalsIgnoreCase("reload") || sub.equalsIgnoreCase("리로드")){ UAQPlugin.get().reloadConfig(); UAQPlugin.get().rewards().load(); UAQPlugin.get().quests().load(); sender.sendMessage(Text.color("&a리로드 완료.")); return true; }
        if(sub.equalsIgnoreCase("npc") || sub.equalsIgnoreCase("npc연결") || sub.equalsIgnoreCase("npc해제")){
            if(args.length>=2 && (sub.equalsIgnoreCase("npc해제") || (args.length>=3 && args[1].equalsIgnoreCase("unlink")))){
                String idStr = sub.equalsIgnoreCase("npc해제")? args[1] : args[2];
                try{ int cid=Integer.parseInt(idStr); UAQPlugin.get().npcs().unlinkCitizens(cid); sender.sendMessage(Text.color("&aCitizens NPC #"+cid+" 언링크.")); }
                catch(NumberFormatException ex){ UAQPlugin.get().npcs().unlinkUUID(idStr); sender.sendMessage(Text.color("&aUUID NPC 언링크.")); } return true;
            }
            // link
            if(args.length>=2){
                String idStr = args[1];
                com.minkang.uaq.service.NPCService.NPCLink link=new com.minkang.uaq.service.NPCService.NPCLink();
                if(args.length>=3){
                    if(args[2].equalsIgnoreCase("quest")){ link.mode=com.minkang.uaq.service.NPCService.NPCLink.Mode.QUEST; link.questId= args.length>=4? args[3] : ""; }
                    else if(args[2].equalsIgnoreCase("attendance") || args[2].equalsIgnoreCase("출석")){ link.mode=com.minkang.uaq.service.NPCService.NPCLink.Mode.ATTENDANCE; }
                    else { link.mode=com.minkang.uaq.service.NPCService.NPCLink.Mode.OPEN_MENU; }
                } else link.mode=com.minkang.uaq.service.NPCService.NPCLink.Mode.OPEN_MENU;
                try{ int cid=Integer.parseInt(idStr); UAQPlugin.get().npcs().linkCitizens(cid, link); sender.sendMessage(Text.color("&aCitizens NPC #"+cid+" 링크 완료.")); }
                catch(NumberFormatException ex){ UAQPlugin.get().npcs().linkUUID(idStr, link); sender.sendMessage(Text.color("&aUUID NPC 링크 완료.")); }
                return true;
            }
            sender.sendMessage(Text.color("&e사용법: /관리 npc연결 <npcId|uuid> openmenu | quest <questId> | attendance"));
            sender.sendMessage(Text.color("&e사용법: /관리 npc해제 <npcId|uuid>"));
            return true;
        }

        if(sub.equalsIgnoreCase("백업") || sub.equalsIgnoreCase("backup")){
            java.io.File data = UAQPlugin.get().getDataFolder();
            String ts = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            java.io.File out = new java.io.File(data, "backup_"+ts);
            out.mkdirs();
            String[] files = new String[]{"config.yml","rewards.yml","quests.yml","players.yml","quest-players.yml","npcs.yml"};
            for(String f: files){
                try{ java.nio.file.Files.copy(new java.io.File(data,f).toPath(), new java.io.File(out,f).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING); }catch(Exception ignored){}
            }
            sender.sendMessage(Text.color("&a백업 완료: ")+out.getName()); return true;
        }
        if(sub.equalsIgnoreCase("테스트보상") || sub.equalsIgnoreCase("testreward")){
            if(args.length<2){ sender.sendMessage(Text.color("&c사용법: /관리 테스트보상 <세트ID> [플레이어]")); return true; }
            String setId = args[1];
            org.bukkit.entity.Player target = (args.length>=3)? org.bukkit.Bukkit.getPlayerExact(args[2]) : (sender instanceof org.bukkit.entity.Player ? (org.bukkit.entity.Player)sender : null);
            if(target==null){ sender.sendMessage(Text.color("&c대상 플레이어가 필요합니다.")); return true; }
            UAQPlugin.get().rewards().applySet(target, setId);
            sender.sendMessage(Text.color("&a보상 적용: ")+setId+" -> "+target.getName()); return true;
        }
        if(sub.equalsIgnoreCase("보호권부여")){
            if(args.length<3){ sender.sendMessage(Text.color("&c사용법: /관리 보호권부여 <플레이어> <수량>")); return true; }
            org.bukkit.entity.Player target = org.bukkit.Bukkit.getPlayerExact(args[1]);
            int amt=1; try{ amt=Integer.parseInt(args[2]); }catch(Exception ignored){}
            if(target==null){ sender.sendMessage(Text.color("&c플레이어가 접속 중이 아닙니다.")); return true; }
            UAQPlugin.get().attendance().addProtector(target.getUniqueId(), amt);
            sender.sendMessage(Text.color("&b결석 보호권 부여: ")+target.getName()+" x"+amt); return true;
        }

        if(sub.equalsIgnoreCase("랭킹리셋")){
            if(args.length<2){ sender.sendMessage(Text.color("&c사용법: /관리 랭킹리셋 <weekly|season>")); return true; }
            if(args[1].equalsIgnoreCase("weekly")){ UAQPlugin.get().lb().resetWeekly(); sender.sendMessage(Text.color("&a주간 포인트 리셋 완료")); return true; }
            if(args[1].equalsIgnoreCase("season")){ UAQPlugin.get().lb().resetSeason(); sender.sendMessage(Text.color("&a시즌 포인트 리셋 완료")); return true; }
            return true; }
        if(sub.equalsIgnoreCase("포인트부여")){
            if(args.length<3){ sender.sendMessage(Text.color("&c사용법: /관리 포인트부여 <플레이어> <수량>")); return true; }
            org.bukkit.entity.Player target = org.bukkit.Bukkit.getPlayerExact(args[1]);
            int amt=1; try{ amt=Integer.parseInt(args[2]); }catch(Exception ignored){}
            if(target==null){ sender.sendMessage(Text.color("&c플레이어가 접속 중이 아닙니다.")); return true; }
            UAQPlugin.get().attendance().addPoints(target.getUniqueId(), amt);
            sender.sendMessage(Text.color("&d포인트 부여: &f")+target.getName()+" x"+amt); return true;
        }
        
        if(sub.equalsIgnoreCase("도감설정열기")){
            if(!(sender instanceof org.bukkit.entity.Player)){ sender.sendMessage(Text.color("&c플레이어만 사용 가능합니다.")); return true; }
            com.minkang.uaq.gui.MenuListener.open((org.bukkit.entity.Player)sender, new com.minkang.uaq.gui.DexRewardConfigMenu((org.bukkit.entity.Player)sender)); return true;
        }
        if(sub.equalsIgnoreCase("도감보상추가")){
            if(args.length<3){ sender.sendMessage(Text.color("&c사용법: /관리 도감보상추가 <퍼센트> <세트ID>")); return true; }
            int per=Integer.parseInt(args[1]); String setId=args[2]; UAQPlugin.get().getConfig().set("dex.thresholds."+per, setId); UAQPlugin.get().saveConfig(); sender.sendMessage(Text.color("&a도감 보상 등록: "+per+"% -> "+setId)); return true;
        }
        if(sub.equalsIgnoreCase("도감보상삭제")){
            if(args.length<2){ sender.sendMessage(Text.color("&c사용법: /관리 도감보상삭제 <퍼센트>")); return true; }
            int per=Integer.parseInt(args[1]); UAQPlugin.get().getConfig().set("dex.thresholds."+per, null); UAQPlugin.get().saveConfig(); sender.sendMessage(Text.color("&a도감 보상 삭제: "+per+"%")); return true;
        }
        if(sub.equalsIgnoreCase("도감진행설정")){
            if(args.length<3){ sender.sendMessage(Text.color("&c사용법: /관리 도감진행설정 <플레이어> <퍼센트>")); return true; }
            org.bukkit.entity.Player t=org.bukkit.Bukkit.getPlayerExact(args[1]); if(t==null){ sender.sendMessage(Text.color("&c플레이어가 접속 중이 아닙니다.")); return true; }
            int per=Integer.parseInt(args[2]); UAQPlugin.get().dex().setPercent(t.getUniqueId(), per); sender.sendMessage(Text.color("&a설정 완료: "+t.getName()+" -> "+per+"%")); return true;
        }
        if(sub.equalsIgnoreCase("세이브")){
            UAQPlugin.get().quests().savePlayers(); UAQPlugin.get().attendance().save(); sender.sendMessage(Text.color("&a저장 완료")); return true;
        }
        if(sub.equalsIgnoreCase("로그토글")){
            boolean cur = UAQPlugin.get().getConfig().getBoolean("debug", false);
            UAQPlugin.get().getConfig().set("debug", !cur); UAQPlugin.get().saveConfig();
            sender.sendMessage(Text.color("&7디버그 로그: " + (!cur))); return true;
        }

        return ProgressCommand.handle(sender, args);
    }
    @Override public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){
        if(args.length==1) return new ArrayList<>(java.util.Arrays.asList("도움말","reload","리로드","npc연결","npc해제","진행"));
        if(args.length==3 && (args[0].equalsIgnoreCase("npc연결") || args[0].equalsIgnoreCase("npc") )) return new ArrayList<>(java.util.Arrays.asList("openmenu","quest","attendance","출석"));
        return java.util.Collections.emptyList();
    }
}
