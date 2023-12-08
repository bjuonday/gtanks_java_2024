package amalgama.network;

import amalgama.Global;
import amalgama.json.lobby.CreateBattleModel;
import amalgama.json.lobby.ShowBattleInfoModel;
import amalgama.network.netty.TransferProtocol;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Arrays;

public class LobbyHandler extends Handler {
    private final ObjectMapper mapper;
    public LobbyHandler(TransferProtocol network) {
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
            else if (command.args[1].equals("check_battleName_for_forbidden_words") && command.args.length == 3) {
                String nameForCheck = command.args[2].trim();
                if (nameForCheck.isEmpty())
                    nameForCheck = "Battle";

                net.send(Type.LOBBY, "check_battle_name", nameForCheck);
            }
            else if (command.args[1].equals("try_create_battle_dm") && command.args.length == 12) {
                System.out.println(Arrays.toString(command.args));
                CreateBattleModel createBattleModel = new CreateBattleModel();
                createBattleModel.name = command.args[2];
                createBattleModel.mapId = command.args[3];
                int time = Integer.parseInt(command.args[4]);
                int kills = Integer.parseInt(command.args[5]);
                createBattleModel.maxPeople = Integer.parseInt(command.args[6]);
                createBattleModel.minRank = Integer.parseInt(command.args[7]);
                createBattleModel.maxRank = Integer.parseInt(command.args[8]);
                boolean isPrivate = command.args[9].equals("true");
                createBattleModel.isPaid = command.args[10].equals("true");
                boolean isDrop = command.args[11].equals("true");
                createBattleModel.bluePeople = 0;
                createBattleModel.redPeople = 0;
                createBattleModel.countPeople = 0;
                createBattleModel.team = false;

                createBattleModel.battleId = Global.createBattle(
                        createBattleModel.name,
                        "user",
                        "dm",
                        createBattleModel.mapId,
                        time,
                        createBattleModel.isPaid,
                        createBattleModel.maxPeople,
                        createBattleModel.maxRank,
                        createBattleModel.minRank,
                        kills,
                        false,
                        false,
                        !isDrop
                );

                net.broadcast("lobby", Type.LOBBY, "create_battle", mapper.writeValueAsString(createBattleModel));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}