
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.configuration.file.YamlConfiguration; import java.io.File; import java.io.IOException; import java.util.*;
public class NPCService {
    private final UAQPlugin plugin; private File file; private YamlConfiguration cfg; private final Map<Integer,NPCLink> citizens=new HashMap<>(); private final Map<String,NPCLink> uuids=new HashMap<>();
    public static class NPCLink { public enum Mode { OPEN_MENU, QUEST, ATTENDANCE } public Mode mode; public String questId; }
    public NPCService(UAQPlugin plugin){ this.plugin=plugin; load(); }
    public void load(){ file=new File(plugin.getDataFolder(),"npcs.yml"); cfg=YamlConfiguration.loadConfiguration(file); citizens.clear(); uuids.clear();
        if(cfg.isConfigurationSection("npcs.citizens")) for(String k: cfg.getConfigurationSection("npcs.citizens").getKeys(false)){ try{ int id=Integer.parseInt(k); NPCLink l=read("npcs.citizens."+k); citizens.put(id,l);}catch(NumberFormatException ignored){} }
        if(cfg.isConfigurationSection("npcs.uuid")) for(String k: cfg.getConfigurationSection("npcs.uuid").getKeys(false)){ uuids.put(k, read("npcs.uuid."+k)); } }
    private NPCLink read(String path){ NPCLink l=new NPCLink(); String m=cfg.getString(path+".mode","OPEN_MENU").toUpperCase(); try{ l.mode=NPCLink.Mode.valueOf(m);}catch(Exception e){ l.mode=NPCLink.Mode.OPEN_MENU; } l.questId=cfg.getString(path+".questId",""); return l; }
    public void save(){ try{ cfg.set("npcs.citizens", null); for(Map.Entry<Integer,NPCLink> e: citizens.entrySet()){ String p="npcs.citizens."+e.getKey(); cfg.set(p+".mode", e.getValue().mode.name()); cfg.set(p+".questId", e.getValue().questId); }
            cfg.set("npcs.uuid", null); for(Map.Entry<String,NPCLink> e: uuids.entrySet()){ String p="npcs.uuid."+e.getKey(); cfg.set(p+".mode", e.getValue().mode.name()); cfg.set(p+".questId", e.getValue().questId); } cfg.save(file);} catch(IOException ex){ ex.printStackTrace(); } }
    public NPCLink getByCitizens(int id){ return citizens.get(id); } public NPCLink getByUUID(String uuid){ return uuids.get(uuid); }
    public void linkCitizens(int id, NPCLink link){ citizens.put(id, link); save(); } public void unlinkCitizens(int id){ citizens.remove(id); save(); }
    public void linkUUID(String uuid, NPCLink link){ uuids.put(uuid, link); save(); } public void unlinkUUID(String uuid){ uuids.remove(uuid); save(); }
}
