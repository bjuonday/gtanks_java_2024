package amalgama.battle;

import amalgama.lobby.Battle;
import amalgama.models.SpawnPositionModel;
import amalgama.network.netty.TransferProtocol;

public class BattlePlayerController {
    public TransferProtocol net;
    public Battle currentBattle;
    public SpawnPositionModel lastSpawnModel;

    public BattlePlayerController(TransferProtocol protocol, Battle battle) {
        this.net = protocol;
        this.currentBattle = battle;
    }
}
