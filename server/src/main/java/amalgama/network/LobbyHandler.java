package amalgama.network;

import amalgama.Global;
import amalgama.json.garage.SendGarageModel;
import amalgama.json.lobby.CreateBattleModel;
import amalgama.json.lobby.ShowBattleInfoModel;
import amalgama.lobby.Battle;
import amalgama.network.managers.GarageManager;
import amalgama.network.netty.TransferProtocol;
import amalgama.network.secure.Grade;
import amalgama.network.secure.Limits;
import amalgama.utils.RankUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LobbyHandler extends Handler {
    private final ObjectMapper mapper;
    public LobbyHandler(TransferProtocol network) {
        super(network);
        mapper = new ObjectMapper();
    }

    private boolean createBattleHook() {
        if (!net.vrs.tryCreateBattle()) {
            net.send(Type.LOBBY, "server_message", "Вы можете создавать не более " + Limits.MAX_BATTLES_IN_INTERVAL + " битв в течении " + Limits.BATTLES_INTERVAL / 60 + " минут.");
            return false;
        }
        return true;
    }

    @Override
    public void handle(Command command) {
        if (!net.client.authorized)
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
                if (!createBattleHook())
                    return;
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

                if (createBattleModel.maxRank < createBattleModel.minRank ||
                    createBattleModel.maxRank > 27 ||
                    createBattleModel.minRank < 1 ||
                    createBattleModel.maxPeople < 2 ||
                    createBattleModel.name.trim().isEmpty()) {
                    net.vrs.registerAct("custom_battle", Grade.DETRIMENTAL);
                    return;
                }

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
            else if (command.args[1].equals("try_create_battle_tdm") && command.args.length == 3) {
                if (!createBattleHook())
                    return;
                JSONParser parser = new JSONParser();
                JSONObject rJson = (JSONObject) parser.parse(command.args[2]);
                JSONObject sJson = new JSONObject();
                sJson.put("mapId", (String) rJson.get("mapId"));
                sJson.put("name", (String) rJson.get("gameName"));
                sJson.put("team", true);
                sJson.put("redPeople", 0);
                sJson.put("bluePeople", 0);
                sJson.put("countPeople", 0);
                sJson.put("maxPeople", (Long) rJson.get("numPlayers"));
                sJson.put("minRank", (Long) rJson.get("minRang"));
                sJson.put("maxRank", (Long) rJson.get("maxRang"));
                sJson.put("isPaid", (boolean) rJson.get("pay"));
                boolean autoBalance = (boolean) rJson.get("autoBalance");
                int kills = ((Long) rJson.get("numKills")).intValue();
                boolean isPrivate = (boolean) rJson.get("privateBattle");
                int time = ((Long) rJson.get("time")).intValue();
                boolean inventory = (boolean) rJson.get("inventory");
                boolean ff = (boolean) rJson.get("frielndyFire");
                String id = Global.createBattle((String) sJson.get("name"), "user", "tdm", (String) sJson.get("mapId"), time, (boolean) sJson.get("isPaid"), ((Long) sJson.get("maxPeople")).intValue(), ((Long) sJson.get("maxRank")).intValue(), ((Long) sJson.get("minRank")).intValue(), kills, autoBalance, ff, !inventory);
                sJson.put("battleId", id);
                net.broadcast("lobby", Type.LOBBY, "create_battle", sJson.toJSONString());
            }
            else if (command.args[1].equals("try_create_battle_ctf") && command.args.length == 3) {
                if (!createBattleHook())
                    return;
                JSONParser parser = new JSONParser();
                JSONObject rJson = (JSONObject) parser.parse(command.args[2]);
                JSONObject sJson = new JSONObject();
                sJson.put("mapId", (String) rJson.get("mapId"));
                sJson.put("name", (String) rJson.get("gameName"));
                sJson.put("team", true);
                sJson.put("redPeople", 0);
                sJson.put("bluePeople", 0);
                sJson.put("countPeople", 0);
                sJson.put("maxPeople", (Long) rJson.get("numPlayers"));
                sJson.put("minRank", (Long) rJson.get("minRang"));
                sJson.put("maxRank", (Long) rJson.get("maxRang"));
                sJson.put("isPaid", (boolean) rJson.get("pay"));
                boolean autoBalance = (boolean) rJson.get("autoBalance");
                int flags = ((Long) rJson.get("numFlags")).intValue();
                boolean isPrivate = (boolean) rJson.get("privateBattle");
                int time = ((Long) rJson.get("time")).intValue();
                boolean inventory = (boolean) rJson.get("inventory");
                boolean ff = (boolean) rJson.get("frielndyFire");
                String id = Global.createBattle((String) sJson.get("name"), "user", "ctf", (String) sJson.get("mapId"), time, (boolean) sJson.get("isPaid"), ((Long) sJson.get("maxPeople")).intValue(), ((Long) sJson.get("maxRank")).intValue(), ((Long) sJson.get("minRank")).intValue(), flags, autoBalance, ff, !inventory);
                sJson.put("battleId", id);
                net.broadcast("lobby", Type.LOBBY, "create_battle", sJson.toJSONString());
            }
            else if (command.args[1].equals("try_create_battle_dom") && command.args.length == 3) {
                if (!createBattleHook())
                    return;
                JSONParser parser = new JSONParser();
                JSONObject rJson = (JSONObject) parser.parse(command.args[2]);
                JSONObject sJson = new JSONObject();
                sJson.put("mapId", (String) rJson.get("mapId"));
                sJson.put("name", (String) rJson.get("gameName"));
                sJson.put("team", true);
                sJson.put("redPeople", 0);
                sJson.put("bluePeople", 0);
                sJson.put("countPeople", 0);
                sJson.put("maxPeople", (Long) rJson.get("numPlayers"));
                sJson.put("minRank", (Long) rJson.get("minRang"));
                sJson.put("maxRank", (Long) rJson.get("maxRang"));
                sJson.put("isPaid", (boolean) rJson.get("pay"));
                boolean autoBalance = (boolean) rJson.get("autoBalance");
                int score = ((Long) rJson.get("numPointsScore")).intValue();
                boolean isPrivate = (boolean) rJson.get("privateBattle");
                int time = ((Long) rJson.get("time")).intValue();
                boolean inventory = (boolean) rJson.get("inventory");
                boolean ff = (boolean) rJson.get("frielndyFire");
                String id = Global.createBattle((String) sJson.get("name"), "user", "dom", (String) sJson.get("mapId"), time, (boolean) sJson.get("isPaid"), ((Long) sJson.get("maxPeople")).intValue(), ((Long) sJson.get("maxRank")).intValue(), ((Long) sJson.get("minRank")).intValue(), score, autoBalance, ff, !inventory);
                sJson.put("battleId", id);
                net.broadcast("lobby", Type.LOBBY, "create_battle", sJson.toJSONString());
            }
            else if (command.args[1].equals("get_garage_data")) {
                SendGarageModel sendGarageModel = GarageManager.getGarage(net.client.userData);
                String garage = mapper.writeValueAsString(sendGarageModel.garage);
                String market = mapper.writeValueAsString(sendGarageModel.market);
                net.send(Type.GARAGE, "init_garage_items", "{\"items\":" + garage + "}");
                net.send(Type.GARAGE, "init_market", "{\"items\":" + market + "}");
            }
            else if (command.args[1].equals("get_data_init_battle_select")) {
                lobbyManager.initBattles();
            }
            else if (command.args[1].equals("show_profile")) {
                JSONObject json = new JSONObject();
                json.put("emailNotice", false);
                json.put("isComfirmEmail", false);
                net.send(Type.LOBBY, "show_profile", json.toJSONString());
            }
            else if (command.args[1].equals("enter_battle") && command.args.length > 2) {
                String battleId = command.args[2];
                Battle battle = Global.battles.get(battleId);
                if (battle == null) {
                    net.vrs.registerAct("attempt_enter_unknown_battle", Grade.DETRIMENTAL);
                    return;
                }
                int rank = RankUtils.getRankFromScore(net.client.userData.getScore());
                if (battle.minRank > rank || rank > battle.maxRank || battle.users.size() >= battle.maxPeople) {
                    net.vrs.registerAct("attempt_enter_unavailable_battle", Grade.SUSPICIOUS);
                    return;
                }
                if (net.client.currentBattleId != null) return;
                battle.service.addPlayer(net, "NONE");
            }
            else if (command.args[1].equals("enter_battle_team") && command.args.length == 4) {
                String battleId = command.args[2];
                Battle battle = Global.battles.get(battleId);
                String team = command.args[3].equals("true") ? "RED" : "BLUE";
                if (battle == null) {
                    net.vrs.registerAct("attempt_enter_unknown_battle", Grade.DETRIMENTAL);
                    return;
                }
                int rank = RankUtils.getRankFromScore(net.client.userData.getScore());
                if (battle.minRank > rank || rank > battle.maxRank || battle.users.size() >= battle.maxPeople) {
                    net.vrs.registerAct("attempt_enter_unavailable_battle", Grade.SUSPICIOUS);
                    return;
                }
                if (battle.autoBalance) {
                    if (team.equals("RED") && battle.redPeople > battle.bluePeople)
                        return;
                    if (team.equals("BLUE") && battle.redPeople < battle.bluePeople)
                        return;
                }
                if (battle.redPeople >= battle.maxPeople || battle.bluePeople >= battle.maxPeople)
                    return;
                if (net.client.currentBattleId != null) return;
                battle.service.addPlayer(net, team);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}