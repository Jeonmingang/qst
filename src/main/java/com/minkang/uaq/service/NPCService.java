package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;

public class NPCService {
    protected final UAQPlugin plugin;
    public NPCService(UAQPlugin plugin){ this.plugin = plugin; }

    public static class NPCLink {
        public String questId;
        public String display;
        public NPCLink(){}
        public NPCLink(String questId, String display){
            this.questId = questId;
            this.display = display;
        }
    }
}
