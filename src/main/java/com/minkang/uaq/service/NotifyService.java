
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.configuration.file.YamlConfiguration; import java.io.File; import java.io.IOException; import java.util.*;
public class NotifyService {
    private final UAQPlugin plugin; private final File file; private final YamlConfiguration data;
    public NotifyService(UAQPlugin plugin){ this.plugin=plugin; this.file=new File(plugin.getDataFolder(),"notify.yml"); this.data=YamlConfiguration.loadConfiguration(file); }
    public void push(java.util.UUID u, String msg){ java.util.List<String> list=data.getStringList("log."+u); list.add(msg); if(list.size()>50) list=list.subList(list.size()-50, list.size()); data.set("log."+u, list); save(); }
    public java.util.List<String> list(java.util.UUID u){ return data.getStringList("log."+u); }
    private void save(){ try{ data.save(file);}catch(IOException ignored){} }
}
