
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.configuration.file.YamlConfiguration; import java.io.File; import java.io.IOException; import java.util.*;
public class PreferencesService {
    private final UAQPlugin plugin; private final File file; private final YamlConfiguration cfg;
    public PreferencesService(UAQPlugin plugin){ this.plugin=plugin; this.file=new File(plugin.getDataFolder(), "preferences.yml"); this.cfg=YamlConfiguration.loadConfiguration(file); }
    private String p(java.util.UUID u, String k){ return "players."+u+"."+k; }
    public boolean get(java.util.UUID u, String key, boolean def){ return cfg.getBoolean(p(u,key), def); }
    public void set(java.util.UUID u, String key, boolean val){ cfg.set(p(u,key), val); try{ cfg.save(file);}catch(IOException ignored){} }
}
