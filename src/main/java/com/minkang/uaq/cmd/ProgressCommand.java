
package com.minkang.uaq.cmd;
import com.minkang.uaq.UAQPlugin; import org.bukkit.Bukkit; import org.bukkit.command.*;
public class ProgressCommand {
    private final UAQPlugin plugin; public ProgressCommand(UAQPlugin plugin){ this.plugin=plugin; } public void register(){}
    public static boolean handle(CommandSender sender, String[] args){
        if(args.length>=4 && (args[0].equalsIgnoreCase("progress") || args[0].equalsIgnoreCase("진행"))){
            String player=args[1]; String qid=args[2]; int amount=1; try{ amount=Integer.parseInt(args[3]); }catch(Exception ignored){}
            if(Bukkit.getPlayerExact(player)!=null){ UAQPlugin.get().quests().addProgress(Bukkit.getPlayerExact(player).getUniqueId(), qid, amount); sender.sendMessage("OK"); } else sender.sendMessage("플레이어가 접속 중이 아닙니다."); return true;
        }
        return false;
    }
}
