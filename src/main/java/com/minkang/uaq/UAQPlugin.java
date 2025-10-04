package com.minkang.uaq;

import org.bukkit.plugin.java.JavaPlugin;

import com.minkang.uaq.service.*;
import com.minkang.uaq.bridge.CitizensBridge;

public class UAQPlugin extends JavaPlugin {
    private static UAQPlugin instance;
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

    @Override public void onEnable(){
        instance = this;
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

    public static UAQPlugin get(){ return instance; }

    public AttendanceService attendance(){ return attendanceService; }
    public RewardService rewards(){ return rewardService; }
    public RewardService rewardService(){ return rewardService; }
    public QuestService quests(){ return questService; }
    public ScoreboardService getScoreboardService(){ return scoreboardService; }
    public TrackerService getTracker(){ return trackerService; }
    public PartyBonusService party(){ return partyBonusService; }
    public CitizensBridge npcs(){ return citizensBridge; }
    public DexService dex(){ return dexService; }
    public PassService pass(){ return passService; }
    public LeaderboardService lb(){ return lbService; }
    public PreferencesService prefs(){ return prefsService; }
    public CouponService coupons(){ return couponService; }
    public ReferralService ref(){ return refService; }
    public NotifyService notifySrv(){ return notifyService; }
    public EconomyBridge economy(){ return economyBridge; }
    public TitlesService titles(){ return titlesService; }
}
