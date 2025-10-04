package com.minkang.uaq.bridge;

import com.minkang.uaq.UAQPlugin;
import com.minkang.uaq.service.NPCService.NPCLink;
import java.util.*;

public class CitizensBridge {
    protected final UAQPlugin plugin;

    private final Map<Integer, NPCLink> byCitizens = new HashMap<>();
    private final Map<String, NPCLink> byUUID = new HashMap<>();

    public CitizensBridge(UAQPlugin plugin){ this.plugin = plugin; }

    public NPCLink getByCitizens(int id){ return byCitizens.get(id); }
    public NPCLink getByUUID(String uuid){ return byUUID.get(uuid); }
    public void unlinkCitizens(int id){ byCitizens.remove(id); }
    public void unlinkUUID(String uuid){ byUUID.remove(uuid); }
    public void linkCitizens(int id, NPCLink link){ byCitizens.put(id, link); }
    public void linkUUID(String uuid, NPCLink link){ byUUID.put(uuid, link); }
}
