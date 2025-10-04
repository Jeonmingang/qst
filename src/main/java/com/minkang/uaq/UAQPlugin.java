package com.minkang.uaq;

import com.minkang.uaq.gui.SafeMenuGuard;
import com.minkang.uaq.gui.MenuListener;
import com.minkang.uaq.service.RewardService;
import org.bukkit.plugin.java.JavaPlugin;

public class UAQPlugin extends JavaPlugin {

    private static UAQPlugin instance;
    private RewardService rewardService;

    public static UAQPlugin get(){ return instance; }

    @Override
    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        saveResource("rewards.yml", false);

        this.rewardService = new RewardService(this);

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new SafeMenuGuard(), this);
    }

    public RewardService rewards(){ return rewardService; }

    // Backward-compat stub
    public Object titles(){ return null; }
}
