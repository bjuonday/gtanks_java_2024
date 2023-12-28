package amalgama.system.timers;

import amalgama.lobby.BattleController;
import amalgama.network.Type;

import java.util.TimerTask;

public class BonusSpawnTask extends TimerTask {
    public String bonusId;
    public BattleController bfService;
    @Override
    public void run() {
        if (bfService == null)
            return;
        bfService.activeBonuses.remove(bonusId);
        bfService.broadcast(Type.BATTLE, "remove_bonus", bonusId);
        BonusSpawner.tasks.remove(bonusId);
    }
}
