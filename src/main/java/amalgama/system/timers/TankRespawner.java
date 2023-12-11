package amalgama.system.timers;

import amalgama.battle.BattlePlayerController;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;

public class TankRespawner {
    public static final Timer timer = new Timer("TankRespawner_timer");
    public static final long DELAY_BEFORE_RESPAWN = 3000L;
    public static final long DELAY_BEFORE_SPAWN = 5000L;
    public static HashMap<BattlePlayerController, TankRespawnTask> tasks = new HashMap<>();

    public static void start(BattlePlayerController ply, boolean instant) {
        if (ply == null || ply.currentBattle == null)
            return;
        TankRespawnTask respawnTask = new TankRespawnTask();
        respawnTask.ply = ply;
        respawnTask.instant = instant;
        tasks.put(ply, respawnTask);
        timer.schedule(respawnTask, instant ? 1L : DELAY_BEFORE_RESPAWN);
    }

    public static void cancel(BattlePlayerController ply) {
        TankRespawnTask respawnTask = tasks.get(ply);
        if (respawnTask == null)
            return;
        Objects.requireNonNullElse(respawnTask.spawnTask, respawnTask).cancel();
        tasks.remove(ply);
    }
}
