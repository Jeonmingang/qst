
package com.minkang.uaq.bridge;

import com.minkang.uaq.UAQPlugin;
import com.minkang.uaq.gui.AttendanceMenu;
import com.minkang.uaq.gui.MenuListener;
import com.minkang.uaq.gui.QuestMenu;
import com.minkang.uaq.service.NPCService;
import com.minkang.uaq.util.Text;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class CitizensBridge implements Listener {
    private final UAQPlugin plugin;
    public CitizensBridge(UAQPlugin plugin){ this.plugin = plugin; }

    @EventHandler public void onNPCRightClick(NPCRightClickEvent e){
        Player p=e.getClicker(); NPCService.NPCLink link=plugin.npcs().getByCitizens(e.getNPC().getId()); handle(p, link);
    }
    @EventHandler public void onEntityInteract(PlayerInteractAtEntityEvent e){
        if (Bukkit.getPluginManager().getPlugin("Citizens") != null) return;
        NPCService.NPCLink link=plugin.npcs().getByUUID(e.getRightClicked().getUniqueId().toString()); handle(e.getPlayer(), link);
    }
    private void handle(Player p, NPCService.NPCLink link){
        if(link==null) return;
        switch (link.mode){
            case OPEN_MENU: MenuListener.open(p, new QuestMenu(p)); break;
            case QUEST:
                if(link.questId==null || link.questId.isEmpty()){ MenuListener.open(p, new QuestMenu(p)); return; }
                if(!plugin.quests().pool().containsKey(link.questId)){ p.sendMessage(Text.color("&c연결된 퀘스트가 존재하지 않습니다: "+link.questId)); return; }
                if(!plugin.quests().getAssigned(p.getUniqueId()).contains(link.questId)){ plugin.quests().assignSpecific(p, link.questId); p.sendMessage(Text.color("&a퀘스트 수주: &f"+plugin.quests().pool().get(link.questId).name)); }
                MenuListener.open(p, new QuestMenu(p)); break;
            case ATTENDANCE: MenuListener.open(p, new AttendanceMenu(p)); break;
        }
    }
}
