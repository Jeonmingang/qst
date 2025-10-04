package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import java.util.*;

public class PreferencesService {
    protected final UAQPlugin plugin;
    private final Map<String, Boolean> store = new HashMap<>();
    public PreferencesService(UAQPlugin plugin){ this.plugin = plugin; }
    private String key(java.util.UUID u, String k){ return u.toString()+":"+k; }
    public boolean get(java.util.UUID u, String k, boolean def){ return store.getOrDefault(key(u,k), def); }
    public void set(java.util.UUID u, String k, boolean v){ store.put(key(u,k), v); }
}
