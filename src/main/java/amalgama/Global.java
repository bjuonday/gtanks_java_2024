package amalgama;

import amalgama.json.garage.GarageItemModel;
import amalgama.json.lobby.LobbyMessageModel;
import amalgama.json.lobby.ShowBattleInfoModel;
import amalgama.json.lobby.UserInBattleModel;
import amalgama.lobby.Battle;
import amalgama.lobby.BattleUser;
import amalgama.lobby.LobbyMessage;
import amalgama.models.RankModel;
import amalgama.network.Client;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Global {
    public static Map<String, Client> clients = new HashMap<>();
    public static List<RankModel> ranks;
    public static Map<String, Battle> battles = new HashMap<>();
    public static List<LobbyMessage> lobbyMessages = new ArrayList<>();
    public static GarageItemModel[] garageItems;

    public static void init() {
        try {
            garageItems = new ObjectMapper().readValue(Paths.get("files\\all_items.json").toFile(), GarageItemModel[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createBattle(String name, String adminRank, String type, String map, int time, boolean isPaid, int maxPeople, int maxRank, int minRank, int maxScore, boolean autoBalance, boolean ff, boolean noBonus) {
        for (int i = 10000; i < 100000; i++) {
            String battleId = i + "@" + name + "@#" + adminRank;
            if (!battles.containsKey(battleId)) {
                Battle battle = new Battle();
                battle.id = battleId;
                battle.type = type;
                battle.name = name;
                battle.mapId = map;
                battle.startTime = System.currentTimeMillis() / 1000L;
                battle.timeLength = time;
                battle.blueScore = 0;
                battle.redScore = 0;
                battle.isPaid = isPaid;
                battle.maxPeople = maxPeople;
                battle.maxRank = maxRank;
                battle.minRank = minRank;
                battle.maxScore = maxScore;
                battle.previewId = map + "_preview";
                battle.autoBalance = autoBalance;
                battle.ff = ff;
                battle.isTeam = !type.equalsIgnoreCase("dm");
                battle.noBonus = noBonus;
                battle.redPeople = 0;
                battle.bluePeople = 0;
                battle.fund = 0;
                battles.put(battle.id, battle);
                battle.start();
                return battle.id;
            }
        }
        return null;
    }

    public static void createBattle(Battle battle, String adminRank) {
        for (int i = 10000; i < 100000; i++) {
            String battleId = i + "@" + battle.name + "@#" + adminRank;
            if (!battles.containsKey(battleId)) {
                battle.id = battleId;
                battles.put(battle.id, battle);
                //battle.start();
                return;
            }
        }
    }

    public static ShowBattleInfoModel getShowBattleInfo(String battleId) {
        if (!battles.containsKey(battleId))
            return null;

        ShowBattleInfoModel ret = new ShowBattleInfoModel();

        ret.users_in_battle = new ArrayList<>();
        for (BattleUser u : battles.get(battleId).users.values()) {
            UserInBattleModel user = new UserInBattleModel();
            user.nickname = u.nickname;
            user.kills = u.kills;
            user.rank = u.rank;
            user.team_type = u.team;
            user.isBot = false;
            ret.users_in_battle.add(user);
        }

        Battle b = battles.get(battleId);

        int currTime = (int) (System.currentTimeMillis() / 1000);
        ret.battleId = b.id;
        ret.name = b.name;
        ret.type = b.type.toUpperCase();
        ret.previewId = b.previewId;
        ret.maxPeople = b.maxPeople;
        ret.minRank = b.minRank;
        ret.maxRank = b.maxRank;
        ret.timeLimit = (int)b.timeLength;
        ret.timeCurrent = (((int)b.startTime + ret.timeLimit) - currTime);
        ret.killsLimit = b.maxScore;
        ret.scoreRed = b.redScore;
        ret.scoreBlue = b.blueScore;
        ret.autobalance = b.autoBalance;
        ret.frielndyFie = b.ff;
        ret.paidBattle = b.isPaid;
        ret.withoutBonuses = b.noBonus;
        ret.userAlreadyPaid = false;
        ret.fullCash = false;
        ret.spectator = false;

        return ret;
    }

    public static LobbyMessageModel putLobbyMessage(String author, String text, int rank, boolean addressed, String targetName, int targetRank) {
        LobbyMessage message = new LobbyMessage();
        LobbyMessageModel model = new LobbyMessageModel();
        message.name = author;
        message.text = text;
        message.rank = rank;
        message.isSystem = false;
        message.isYellow = false;
        message.haveTarget = addressed;
        if (message.haveTarget) {
            message.targetName = targetName;
            message.targetRank = targetRank;
        }

        model.addressed = message.haveTarget;
        model.name = message.name;
        model.message = message.text;
        model.rang = message.rank;
        model.yellow = message.isYellow;
        model.system = message.isSystem;
        model.nameTo = message.targetName;
        model.rangTo = message.targetRank;
        lobbyMessages.add(message);
        return model;
    }

    public static LobbyMessageModel putLobbySystemMessage(String text, boolean yellow) {
        LobbyMessage message = new LobbyMessage();
        LobbyMessageModel model = new LobbyMessageModel();
        message.name = "system";
        message.text = text;
        message.rank = 0;
        message.isSystem = true;
        message.isYellow = yellow;
        message.haveTarget = false;
        message.targetName = null;
        message.targetRank = 0;

        model.addressed = message.haveTarget;
        model.name = message.name;
        model.message = message.text;
        model.rang = message.rank;
        model.yellow = message.isYellow;
        model.system = message.isSystem;
        model.nameTo = message.targetName;
        model.rangTo = message.targetRank;
        lobbyMessages.add(message);
        return model;
    }
}
