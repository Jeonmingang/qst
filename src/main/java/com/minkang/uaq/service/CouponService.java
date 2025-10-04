
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.configuration.ConfigurationSection; import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File; import java.io.IOException; import java.util.*;
public class CouponService {
    private final UAQPlugin plugin; private final File file; private final YamlConfiguration used;
    public CouponService(UAQPlugin plugin){ this.plugin=plugin; this.file=new File(plugin.getDataFolder(), "coupons-used.yml"); this.used=YamlConfiguration.loadConfiguration(file); }
    public boolean redeem(org.bukkit.entity.Player p, String code){
        ConfigurationSection s=plugin.getConfig().getConfigurationSection("coupons."+code);
        if(s==null) return false;
        String key=p.getUniqueId().toString()+"."+code;
        if(used.getBoolean(key,false)) return false;
        int points=s.getInt("points",0); String setId=s.getString("rewardSet",""); String cmd=s.getString("command","");
        if(points>0) plugin.attendance().addPoints(p.getUniqueId(), points);
        if(setId!=null && !setId.isEmpty()) plugin.rewards().applySet(p,setId);
        if(cmd!=null && !cmd.isEmpty()) org.bukkit.Bukkit.dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), cmd.replace("{player}", p.getName()));
        used.set(key,true); try{ used.save(file);}catch(IOException ignored){}
        return true;
    }
}
