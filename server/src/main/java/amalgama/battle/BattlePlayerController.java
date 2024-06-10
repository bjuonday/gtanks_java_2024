package amalgama.battle;

import amalgama.database.UserMount;
import amalgama.lobby.Battle;
import amalgama.models.HullModificationModel;
import amalgama.models.SpawnPositionModel;
import amalgama.models.TurretModificationModel;
import amalgama.network.Type;
import amalgama.network.netty.TransferProtocol;

public class BattlePlayerController {
    public TransferProtocol net;
    public Battle currentBattle;
    public SpawnPositionModel lastSpawnModel;
    public Tank tank = null;

    public BattlePlayerController(TransferProtocol protocol, Battle battle) {
        this.net = protocol;
        this.currentBattle = battle;
    }

    public void setupTank(String nick, TurretModificationModel turret, HullModificationModel hull, UserMount mount) {
        tank = new Tank(nick, turret, hull, mount);
    }

    public void updateStats() {
         String statsJson = currentBattle.service.makeStatisticJson(net);
         currentBattle.service.broadcast(Type.BATTLE, "update_player_statistic", statsJson);
    }
}
