package amalgama.network;

import amalgama.Global;
import amalgama.lobby.Battle;
import amalgama.network.netty.TransferProtocol;
import amalgama.network.secure.Grade;
import org.json.simple.JSONObject;

import java.util.ArrayList;
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
        else if (command.args[1].equals("move") && command.args.length > 4) {
            List<Double> list = new ArrayList<>();
            try {
                for (var i : command.args[2].split("@"))
                    list.add(Double.parseDouble(i));
            } catch (NumberFormatException e) {
                net.vrs.registerAct("unexpected_move_argument", Grade.DETRIMENTAL);
                return;
            }
            battle.service.move(net, list, Integer.parseInt(command.args[4]), Double.parseDouble(command.args[3]));
        }
        else if (command.args[1].equals("fire")) {
            try {
                battle.service.fire(net, command.args[2]);
            } catch (Exception e) { e.printStackTrace(); }
        }
        else if (command.args[1].equals("suicide")) {
            battle.service.suicide(net);
        }
        else if (command.args[1].equals("i_exit_from_battle")) {
            try {
                battle.service.removeUser(net);
                lobbyManager.initEffectModel();
                lobbyManager.initBattles();
                lobbyManager.initChat();
            } catch (Exception ignored) {}
        }
    }
}
