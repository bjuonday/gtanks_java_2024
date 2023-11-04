package amalgama.network;

import amalgama.Global;
import amalgama.json.lobby.ShowBattleInfoModel;
import org.codehaus.jackson.map.ObjectMapper;

public class LobbyHandler extends Handler {
    private ObjectMapper mapper;
    public LobbyHandler(Network network) {
        super(network);
        mapper = new ObjectMapper();
    }

    @Override
    public void handle(Command command) {
        if (!net.client.authorized)
            return;

        if (command.args.length == 1)
            return;

        try {
            if (command.args[1].equals("get_show_battle_info")) {
                if (command.args.length < 3 || command.args[2].isEmpty())
                    return;

                ShowBattleInfoModel showBattleInfoModel = Global.getShowBattleInfo(command.args[2]);
                net.send(Type.LOBBY, "show_battle_info", mapper.writeValueAsString(showBattleInfoModel));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}