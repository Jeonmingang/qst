
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import com.minkang.uaq.model.*; import org.bukkit.*; import org.bukkit.configuration.file.YamlConfiguration; import org.bukkit.entity.Player; import org.bukkit.inventory.ItemStack;
import java.io.File; import java.io.IOException; import java.util.*;
public class RewardService {
    private final UAQPlugin plugin; private File rewardsFile; private YamlConfiguration rewardsCfg; private final Map<String,RewardSet> sets=new LinkedHashMap<>();
    public RewardService(UAQPlugin plugin){ this.plugin=plugin; load(); }
    public void load(){ rewardsFile=new File(plugin.getDataFolder(),"rewards.yml"); rewardsCfg=YamlConfiguration.loadConfiguration(rewardsFile); sets.clear();
        if(rewardsCfg.isConfigurationSection("rewardSets")) for(String id: rewardsCfg.getConfigurationSection("rewardSets").getKeys(false)){ sets.put(id, RewardSet.from(id, rewardsCfg.getConfigurationSection("rewardSets."+id))); } }
    public void save(){ try{ YamlConfiguration out=new YamlConfiguration();
        for(Map.Entry<String,RewardSet> e: sets.entrySet()){ String path="rewardSets."+e.getKey(); RewardSet rs=e.getValue(); out.set(path+".name", rs.name);
            java.util.List<Map<String,Object>> list=new java.util.ArrayList<>(); for(Reward r: rs.rewards){ Map<String,Object> m=new LinkedHashMap<>(); m.put("type", r.type.name());
                if(r.type==RewardType.MONEY) m.put("amount", r.amount); if(r.type==RewardType.COMMAND) m.put("command", r.command);
                if(r.type==RewardType.ITEM && r.item!=null) m.put("item", r.item); list.add(m);} out.set(path+".rewards", list);} out.save(rewardsFile); rewardsCfg=out; } catch(IOException ex){ ex.printStackTrace(); } }
    public Collection<RewardSet> allSets(){ return sets.values(); } public RewardSet get(String id){ return sets.get(id); } public RewardSet ensure(String id){ return sets.computeIfAbsent(id,k->{RewardSet rs=new RewardSet(); rs.id=k; rs.name="Set "+k; return rs;}); }
    public void delete(String id){ sets.remove(id); save(); }
    public void applySet(Player p, String id){ RewardSet rs=sets.get(id); if(rs==null) return; for(Reward r: rs.rewards){ switch(r.type){
        case MONEY: plugin.economy().deposit(p.getName(), r.amount); break; case COMMAND: String cmd=r.command!=null? r.command.replace("{player}", p.getName()) : ""; if(!cmd.isEmpty()) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd); break;
        case ITEM: ItemStack it=r.item!=null? r.item.clone() : new ItemStack(Material.AIR);
            if(it.getType()!=Material.AIR){
                java.util.HashMap<Integer, ItemStack> left=p.getInventory().addItem(it); com.minkang.uaq.UAQPlugin.get().notifySrv().push(p.getUniqueId(), "아이템 보상 수령: "+it.getType());
                if(!left.isEmpty()){
                    org.bukkit.inventory.ItemStack rem = left.values().iterator().next();
                    org.bukkit.inventory.ItemStack box = it.clone(); box.setAmount(rem.getAmount());
                    com.minkang.uaq.UAQPlugin.get().mail().add(p.getUniqueId(), box);
                    p.sendMessage(com.minkang.uaq.util.Text.color("&7인벤토리가 가득 차 우편함으로 보관했습니다."));
                }
            } break;
        case PROTECT: com.minkang.uaq.UAQPlugin.get().attendance().addProtector(p.getUniqueId(), (int)Math.max(1, r.amount)); p.sendMessage(com.minkang.uaq.util.Text.color("&b결석 보호권 +" + (int)Math.max(1, r.amount))); break;
        case POINTS: {
                int base=(int)Math.max(1, r.amount);
                java.time.LocalDate today = java.time.LocalDate.now(java.time.ZoneId.of(com.minkang.uaq.UAQPlugin.get().getConfig().getString("timezone","Asia/Seoul")));
                double mult=1.0;
                if(today.getDayOfWeek().getValue()>=6) mult*=com.minkang.uaq.UAQPlugin.get().getConfig().getDouble("multipliers.weekendPoints",1.0);
                mult*=com.minkang.uaq.UAQPlugin.get().getConfig().getDouble("multipliers.dates."+today.toString(), 1.0);
                int chance=com.minkang.uaq.UAQPlugin.get().getConfig().getInt("lucky.chance",0);
                if(chance>0 && new java.util.Random().nextInt(100)<chance){ mult*=com.minkang.uaq.UAQPlugin.get().getConfig().getDouble("lucky.multiplier",2.0); p.sendMessage(com.minkang.uaq.util.Text.color("&6럭키! 배율 적용")); }
                int add=(int)Math.max(1, Math.round(base*mult));
                com.minkang.uaq.UAQPlugin.get().attendance().addPoints(p.getUniqueId(), add);
                com.minkang.uaq.UAQPlugin.get().lb().addWeeklyPoints(p.getUniqueId(), add);
                com.minkang.uaq.UAQPlugin.get().lb().addSeasonPoints(p.getUniqueId(), add);
                p.sendMessage(com.minkang.uaq.util.Text.color("&d출석 포인트 +"+add));
            } break; case PASS_XP: com.minkang.uaq.UAQPlugin.get().pass().addXP(p.getUniqueId(), (int)Math.max(1,r.amount)); com.minkang.uaq.UAQPlugin.get().pass().checkLevelUp(p); break; /* TITLE removed */ case /*TITLE*/ MONEY: if(/*removed*/ null!=null){ com.minkang.uaq.UAQPlugin.get().titles().add(p.getUniqueId(), /*removed*/ null); p.sendMessage(com.minkang.uaq.util.Text.color("&a칭호 해제: ")+/*removed*/ null); } break; } } }
    public java.util.List<String> previewLines(String id, int max){ java.util.List<String> out=new java.util.ArrayList<>(); RewardSet rs=sets.get(id); if(rs==null){ out.add("&7(보상없음)"); return out; }
        int shown=0; for(Reward r: rs.rewards){ if(shown>=max){ out.add("&7..."); break;} switch(r.type){ case MONEY: out.add("&6돈 &ex"+(int)r.amount); break;
                case COMMAND: out.add("&b명령어 &7"+(r.command!=null? r.command.replace("{player}","{p}") : "")); break;
                case ITEM: if(r.item!=null){ String name=r.item.hasItemMeta() && r.item.getItemMeta().hasDisplayName()? r.item.getItemMeta().getDisplayName() : r.item.getType().name(); out.add("&f아이템 &7"+name+" x"+r.item.getAmount()); } else out.add("&f아이템 &7(지정안됨)"); break; } shown++; }
        if(out.isEmpty()) out.add("&7(보상없음)"); return out; }
}
