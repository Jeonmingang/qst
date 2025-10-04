
package com.minkang.uaq.gui;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.util.*; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.inventory.InventoryClickEvent; import org.bukkit.inventory.Inventory;
public class AdminMenu implements UMenu {
    private final Inventory inv;
    public AdminMenu(){ inv=Bukkit.createInventory(null,27, Text.color("&0UAQ 관리자"));
        inv.setItem(10, Items.button(Material.CHEST, "&e보상 세트 편집", "&7출석/퀘 보상 세트를 GUI로 편집"));
        inv.setItem(12, Items.button(Material.CLOCK, "&b출석 리워드 매핑", "&7요일/스트릭 -> 세트 연결")); inv.setItem(13, Items.button(Material.CHEST_MINECART, "&b퀘스트 보상 매핑", "&7퀘스트별 세트 연결"));
        inv.setItem(14, Items.button(Material.BOOK, "&a퀘스트 목록 편집", "&7퀘스트 기본정보/보상 연결"));
        inv.setItem(16, Items.button(Material.REDSTONE_TORCH, "&c리로드", "&7설정/퀘/보상 리로드")); }
    @Override public Inventory getInventory(){ return inv; }
    @Override public void onClick(InventoryClickEvent e){ Player p=(Player)e.getWhoClicked(); switch(e.getSlot()){
        case 10: MenuListener.open(p,new RewardSetListMenu()); break; case 12: MenuListener.open(p,new AttendanceMapMenu()); break;
        case 13: MenuListener.open(p,new QuestRewardMapMenu()); break; case 14: MenuListener.open(p,new QuestListMenu()); break; case 16: UAQPlugin.get().rewards().load(); UAQPlugin.get().quests().load(); p.sendMessage(Text.color("&a리로드 완료.")); p.closeInventory(); break; } }
}
