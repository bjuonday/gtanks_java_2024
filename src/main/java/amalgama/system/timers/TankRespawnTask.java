package amalgama.system.timers;

import amalgama.battle.BattlePlayerController;
import amalgama.models.MapPointModel;
import amalgama.models.SpawnPositionModel;
import amalgama.network.Type;

import java.util.TimerTask;

public class TankRespawnTask extends TimerTask {
    public TankSpawnTask spawnTask;
    public BattlePlayerController ply;
    public boolean instant; //first time spawn

    @Override
    public void run() {
        if (ply == null )
            return;
        if (instant) {
            SpawnPositionModel pos = ply.lastSpawnModel;
            ply.net.send(Type.BATTLE, "prepare_to_spawn", ply.net.client.userData.getLogin(), pos.x + "@" + pos.y + "@" + pos.z + "@" + pos.rot);
        }
        else {
            MapPointModel pos = ply.currentBattle.service.generateNextSpawnPos(ply.net);
            ply.lastSpawnModel.x = pos.x;
            ply.lastSpawnModel.y = pos.y;
            ply.lastSpawnModel.z = pos.z;
            ply.lastSpawnModel.rot = pos.a;
            ply.net.send(Type.BATTLE, "prepare_to_spawn", ply.net.client.userData.getLogin(), pos.x + "@" + pos.y + "@" + pos.z + "@" + pos.a);
        }
        spawnTask = new TankSpawnTask();
        spawnTask.respawnTask = this;
        TankRespawner.timer.schedule(spawnTask, TankRespawner.DELAY_BEFORE_SPAWN);
    }
}
