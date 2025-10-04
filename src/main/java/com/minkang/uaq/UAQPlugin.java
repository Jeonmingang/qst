package com.minkang.uaq;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import com.minkang.uaq.service.*;
import com.minkang.uaq.bridge.CitizensBridge;

public class UAQPlugin extends JavaPlugin {

    private static UAQPlugin instance;

    // Core services (some may already exist in your project; these are wiring fields)
    private AttendanceService attendanceService;
    private RewardService rewardService;
    private QuestService questService;
    private ScoreboardService scoreboardService;
    private TrackerService trackerService;
    private PartyBonusService partyBonusService;
    private CitizensBridge citizensBridge;
    private DexService dexService;
    private PassService passService;
    private LeaderboardService lbService;
    private PreferencesService prefsService;
    private CouponService couponService;
    private ReferralService refService;
    private NotifyService notifyService;
    private EconomyBridge economyBridge;
    private TitlesService titlesService;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize stubs if null (won't override your existing singletons if you rewire later)
        if (attendanceService == null) attendanceService = new AttendanceService(this);
        if (rewardService == null)     rewardService     = new RewardService(this);
        if (questService == null)      questService      = new QuestService(this);
        if (scoreboardService == null) scoreboardService = new ScoreboardService(this);
        if (trackerService == null)    trackerService    = new TrackerService(this);
        if (partyBonusService == null) partyBonusService = new PartyBonusService(this);
        if (citizensBridge == null)    citizensBridge    = new CitizensBridge(this);
        if (dexService == null)        dexService        = new DexService(this);
        if (passService == null)       passService       = new PassService(this);
        if (lbService == null)         lbService         = new LeaderboardService(this);
        if (prefsService == null)      prefsService      = new PreferencesService(this);
        if (couponService == null)     couponService     = new CouponService(this);
        if (refService == null)        refService        = new ReferralService(this);
        if (notifyService == null)     notifyService     = new NotifyService(this);
        if (economyBridge == null)     economyBridge     = new EconomyBridge(this);
        if (titlesService == null)     titlesService     = new TitlesService(this);
    }

    public static UAQPlugin get() { return instance; }

    // ======== getters expected across the codebase ========
    public AttendanceService attendance() { return attendanceService; }
    public RewardService rewards() { return rewardService; } // optional alias
    public RewardService rewardService() { return rewardService; } // optional alias
    public QuestService quests() { return questService; }
    public ScoreboardService getScoreboardService() { return scoreboardService; }
    public TrackerService getTracker() { return trackerService; }
    public PartyBonusService party() { return partyBonusService; }
    public CitizensBridge npcs() { return citizensBridge; }
    public DexService dex() { return dexService; }
    public PassService pass() { return passService; }
    public LeaderboardService lb() { return lbService; }
    public PreferencesService prefs() { return prefsService; }
    public CouponService coupons() { return couponService; }
    public ReferralService ref() { return refService; }
    public NotifyService notifySrv() { return notifyService; }
    public EconomyBridge economy() { return economyBridge; }
    public TitlesService titles() { return titlesService; }

    // Allow external wiring if you already construct services elsewhere
    public void setAttendanceService(AttendanceService s){ this.attendanceService = s; }
    public void setRewardService(RewardService s){ this.rewardService = s; }
    public void setQuestService(QuestService s){ this.questService = s; }
    public void setScoreboardService(ScoreboardService s){ this.scoreboardService = s; }
    public void setTrackerService(TrackerService s){ this.trackerService = s; }
    public void setPartyBonusService(PartyBonusService s){ this.partyBonusService = s; }
    public void setCitizensBridge(CitizensBridge s){ this.citizensBridge = s; }
    public void setDexService(DexService s){ this.dexService = s; }
    public void setPassService(PassService s){ this.passService = s; }
    public void setLeaderboardService(LeaderboardService s){ this.lbService = s; }
    public void setPreferencesService(PreferencesService s){ this.prefsService = s; }
    public void setCouponService(CouponService s){ this.couponService = s; }
    public void setReferralService(ReferralService s){ this.refService = s; }
    public void setNotifyService(NotifyService s){ this.notifyService = s; }
    public void setEconomyBridge(EconomyBridge s){ this.economyBridge = s; }
    public void setTitlesService(TitlesService s){ this.titlesService = s; }
}
