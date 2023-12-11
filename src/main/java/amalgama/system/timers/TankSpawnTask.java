package amalgama.system.timers;

import amalgama.battle.BattlePlayerController;
import amalgama.network.Type;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.TimerTask;

public class TankSpawnTask extends TimerTask {

    public TankRespawnTask respawnTask;

    @Override
    public void run() {
        BattlePlayerController ply = respawnTask.ply;
        if (ply == null)
            return;
        if (ply.currentBattle == null)
            return;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ply.currentBattle.service.killService.changeHealth(ply, 10000);
            ply.currentBattle.service.broadcast(Type.BATTLE, "spawn", mapper.writeValueAsString(ply.lastSpawnModel));
        } catch (Exception e) { e.printStackTrace(); }
        TankRespawner.tasks.remove(ply);
    }
}
