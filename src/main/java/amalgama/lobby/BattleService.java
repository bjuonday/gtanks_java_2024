package amalgama.lobby;

import amalgama.system.quartz.IQuartzService;
import amalgama.system.quartz.QuartzService;
import amalgama.system.quartz.TimeUnit;

public class BattleService {
    public static final String QUARTZ_GROUP = BattleService.class.getName();
    public final String QUARTZ_NAME;
    public final String QUARTZ_RESTART_NAME;
    private long endTimePoint;
    private Battle battle;

    private IQuartzService quartzService = QuartzService.getInstance();

    public BattleService(Battle battle) {
        this.battle = battle;
        QUARTZ_NAME = "BattleTimer/" + hashCode() + "/" + battle.id;
        QUARTZ_RESTART_NAME = "BattleRestarter/" + hashCode() + "/" + battle.id;
        if (battle.timeLength > 0)
            startTimer(((Long) battle.timeLength).intValue());
    }

    private void startTimer(int seconds) {
        System.out.println("Timer start " + seconds);
        this.endTimePoint = System.currentTimeMillis() + seconds * 1000L;
        this.quartzService.addJob(QUARTZ_NAME, QUARTZ_GROUP, e -> {
            System.out.println("End battle");
            battle.startTime = System.currentTimeMillis() / 1000L;
            //TODO Restart battle
        }, TimeUnit.SECONDS, battle.timeLength);
    }
}
