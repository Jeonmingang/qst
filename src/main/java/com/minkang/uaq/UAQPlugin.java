
package com.minkang.uaq;

import com.minkang.uaq.gui.SafeMenuGuard;
import com.minkang.uaq.cmd.CommandGuard;


import com.minkang.uaq.cmd.AdminCommand;
import com.minkang.uaq.cmd.AttendanceCommand;
import com.minkang.uaq.cmd.ProgressCommand;
import com.minkang.uaq.cmd.QuestCommand;
import com.minkang.uaq.cmd.DexRootCommand;
import com.minkang.uaq.gui.MenuListener;
import com.minkang.uaq.service.AttendanceService;
import com.minkang.uaq.service.NPCService;
import com.minkang.uaq.service.QuestService;
import com.minkang.uaq.service.RewardService;
import com.minkang.uaq.util.EconomyHook;
import com.minkang.uaq.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UAQPlugin extends JavaPlugin {

    private static UAQPlugin instance;
    private RewardService rewardService;
    private QuestService questService;
    private AttendanceService attendanceService;
    private EconomyHook economyHook;
    private NPCService npcService; private com.minkang.uaq.service.DexRewardService dex; private com.minkang.uaq.service.PassService pass;  private com.minkang.uaq.service.ReferralService ref; private com.minkang.uaq.service.NotifyService notifySrv; private com.minkang.uaq.service.PreferencesService prefs; private com.minkang.uaq.service.LeaderboardService lb; private com.minkang.uaq.service.CouponService coupons; private com.minkang.uaq.service.MailboxService mail; private com.minkang.uaq.service.RouletteService roulette; private com.minkang.uaq.service.PartyBonusService party; private com.minkang.uaq.service.TrackService tracker; private com.minkang.uaq.service.ScoreboardService scoreboardService;

    public static UAQPlugin get() { return instance; }

    @Override
    public void onEnable() {
// === Security/Anti-exploit guards ===
try {
    // Ensure default config contains security keys
    this.saveDefaultConfig();

    // Menu guard
    org.bukkit.Bukkit.getPluginManager().registerEvents(
        new com.minkang.uaq.gui.SafeMenuGuard(this, this.getConfig()), this
    );

    // Command guard
    java.util.List<String> aliases = this.getConfig().getStringList("uaq.security.adminCommandAliases");
    String perm = this.getConfig().getString("uaq.security.adminPermission", "uaq.admin");
    long cd = this.getConfig().getLong("uaq.security.commandCooldownMillis", 500L);
    org.bukkit.Bukkit.getPluginManager().registerEvents(
        new com.minkang.uaq.cmd.CommandGuard(this, aliases, perm, cd), this
    );
} catch (Throwable t) {
    getLogger().warning("Security guards init failed: " + t.getMessage());
}

        instance = this;
        saveDefaultConfig();
        saveResource("rewards.yml", false);
        saveResource("quests.yml", false);

        this.economyHook = new EconomyHook(this);
        this.rewardService = new RewardService(this);
        this.questService = new QuestService(this);
        this.attendanceService = new AttendanceService(this);
        this.npcService = new NPCService(this);
        this.dex = new com.minkang.uaq.service.DexRewardService(this);
        this.pass = new com.minkang.uaq.service.PassService(this);
        
        this.ref = new com.minkang.uaq.service.ReferralService(this);
        this.notifySrv = new com.minkang.uaq.service.NotifyService(this);
        this.prefs = new com.minkang.uaq.service.PreferencesService(this);
        this.lb = new com.minkang.uaq.service.LeaderboardService(this);
        this.coupons = new com.minkang.uaq.service.CouponService(this);
        this.mail = new com.minkang.uaq.service.MailboxService(this);
        this.roulette = new com.minkang.uaq.service.RouletteService(this);
        this.party = new com.minkang.uaq.service.PartyBonusService(this);
        this.tracker = new com.minkang.uaq.service.TrackService(this);
        this.scoreboardService = new com.minkang.uaq.service.ScoreboardService(this);
        getServer().getPluginManager().registerEvents(tracker, this);

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new com.minkang.uaq.listeners.JoinListener(this), this);
        getServer().getPluginManager().registerEvents(questService, this);
        getServer().getPluginManager().registerEvents(new com.minkang.uaq.bridge.CitizensBridge(this), this);

        getCommand("attendance").setExecutor(new AttendanceCommand());
        getCommand("quest").setExecutor(new QuestCommand());
        getCommand("uaq").setExecutor(new AdminCommand());
        getCommand("uaq").setTabCompleter(new AdminCommand());
        getCommand("quest").setTabCompleter(new QuestCommand());
        getCommand("attendance").setTabCompleter(new AttendanceCommand());

        new ProgressCommand(this).register();

        Bukkit.getConsoleSender().sendMessage(Text.color("&a[UAQ] Enabled v" + getDescription().getVersion()));
        // autosave every 5 min
        getServer().getScheduler().runTaskTimer(this, ()->{ quests().savePlayers(); attendance().save(); for(org.bukkit.entity.Player p: getServer().getOnlinePlayers()) getScoreboardService().update(p); }, 5*60*20L, 20*10L);
    }

    @Override
    public void onDisable() {
        questService.savePlayers();
        attendanceService.save();
        Bukkit.getConsoleSender().sendMessage(Text.color("&c[UAQ] Disabled"));
    }

    public RewardService rewards() { return rewardService; }
    public QuestService quests() { return questService; }
    public AttendanceService attendance() { return attendanceService; }
    public EconomyHook economy() { return economyHook; }
    public NPCService npcs() { return npcService; }
    public com.minkang.uaq.service.DexRewardService dex(){ return dex; }
    public com.minkang.uaq.service.PassService pass(){ return pass; }
    
    public com.minkang.uaq.service.ReferralService ref(){ return ref; }
    public com.minkang.uaq.service.NotifyService notifySrv(){ return notifySrv; }
    public com.minkang.uaq.service.PreferencesService prefs(){ return prefs; }
    public com.minkang.uaq.service.LeaderboardService lb(){ return lb; }
    public com.minkang.uaq.service.CouponService coupons(){ return coupons; }
    public com.minkang.uaq.service.MailboxService mail(){ return mail; }
    public com.minkang.uaq.service.RouletteService roulette(){ return roulette; }
    public com.minkang.uaq.service.PartyBonusService party(){ return party; }
    public com.minkang.uaq.service.TrackService getTracker(){ return tracker; }
    public com.minkang.uaq.service.ScoreboardService getScoreboardService(){ return scoreboardService; }
}
