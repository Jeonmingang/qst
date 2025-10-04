
package com.minkang.uaq.service;
import com.minkang.uaq.UAQPlugin; import org.bukkit.configuration.file.YamlConfiguration; import java.io.File; import java.io.IOException;
public class ReferralService {
    private final UAQPlugin plugin; private final File file; private final YamlConfiguration data;
    public ReferralService(UAQPlugin plugin){ this.plugin=plugin; this.file=new File(plugin.getDataFolder(),"referrals.yml"); this.data=YamlConfiguration.loadConfiguration(file); }
    public boolean setReferrer(java.util.UUID who, java.util.UUID ref){ if(who.equals(ref)) return false; if(data.contains("who."+who)) return false; data.set("who."+who, ref.toString()); save(); org.bukkit.entity.Player p=plugin.getServer().getPlayer(ref);
        int reward=plugin.getConfig().getInt("referral.bonusPoints", 10); plugin.attendance().addPoints(who, reward); if(p!=null) plugin.attendance().addPoints(ref, reward); return true; }
    public String getReferrer(java.util.UUID who){ return data.getString("who."+who, ""); }
    private void save(){ try{ data.save(file);}catch(IOException ignored){} }
}
