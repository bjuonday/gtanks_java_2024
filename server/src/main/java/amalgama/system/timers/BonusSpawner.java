package amalgama.system.timers;

import amalgama.lobby.BattleController;

import java.util.HashMap;
import java.util.Timer;

public class BonusSpawner {
    private static final Timer TIMER = new Timer("BonusSpawner_timer");
    public static HashMap<String, BonusSpawnTask> tasks = new HashMap<>();

    public static void bonusRemove(BattleController bfService, String bonusId, long time) {
        BonusSpawnTask task = new BonusSpawnTask();
        task.bfService = bfService;
        task.bonusId = bonusId;
        tasks.put(bonusId, task);
        TIMER.schedule(task, time * 1000L);
    }
}
