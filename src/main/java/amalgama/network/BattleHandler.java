package amalgama.network;

import amalgama.Global;
import amalgama.lobby.Battle;
import amalgama.network.netty.TransferProtocol;

import java.util.List;

public class BattleHandler extends Handler {
    private Battle battle;

    public BattleHandler(TransferProtocol network) {
        super(network);
        battle = Global.battles.get(network.client.currentBattleId);
    }

    @Override
    public void handle(Command command) {
        if (!net.client.authorized)
            return;

        if (battle == null) {
            battle = Global.battles.get(net.client.currentBattleId);
            if (battle == null)
                return;
        }

        if (command.args[1].equals("get_init_data_local_tank")) {
            if (net.client.currentBattleId == null)
                return;

            try {
                battle.service.initTank(net);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        else if (command.args[1].equals("activate_tank")) {
            battle.service.activateTank(net);
        }
    }
}
