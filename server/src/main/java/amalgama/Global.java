package amalgama;

import amalgama.json.garage.GarageItemModel;
import amalgama.json.lobby.LobbyMessageModel;
import amalgama.json.lobby.ShowBattleInfoModel;
import amalgama.json.lobby.UserInBattleModel;
import amalgama.lobby.Battle;
import amalgama.lobby.BattleUser;
import amalgama.lobby.LobbyMessage;
import amalgama.models.HullModificationModel;
import amalgama.models.RankModel;
import amalgama.models.TurretModificationModel;
import amalgama.network.Client;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Global {
    public static Map<String, Client> clients = new HashMap<>();
    public static List<RankModel> ranks;
    public static Map<String, Battle> battles = new HashMap<>();
    public static Map<String, HullModificationModel[]> hulls_config = new HashMap<>();
    public static Map<String, TurretModificationModel[]> turrets_config = new HashMap<>();
    public static List<LobbyMessage> lobbyMessages = new ArrayList<>();
    public static GarageItemModel[] garageItems;
    public static String initSfxData;
    public static String initShotsData;

    public static void init() {
        try {
            garageItems = new ObjectMapper().readValue(Paths.get("files\\all_items.json").toFile(), GarageItemModel[].class);
            BufferedReader reader = new BufferedReader(new FileReader("files\\sfx_data.json"));
            initSfxData = String.join("", reader.lines().toList());
            reader = new BufferedReader(new FileReader("files\\shots_data.json"));
            initShotsData = String.join("", reader.lines().toList());
            initTankParts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initTankParts() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        hulls_config.put("wasp", mapper.readValue(Paths.get("files\\hulls\\wasp.json").toFile(), HullModificationModel[].class));
        hulls_config.put("hunter", mapper.readValue(Paths.get("files\\hulls\\hunter.json").toFile(), HullModificationModel[].class));
        hulls_config.put("titan", mapper.readValue(Paths.get("files\\hulls\\titan.json").toFile(), HullModificationModel[].class));
        hulls_config.put("dictator", mapper.readValue(Paths.get("files\\hulls\\dictator.json").toFile(), HullModificationModel[].class));
        hulls_config.put("hornet", mapper.readValue(Paths.get("files\\hulls\\hornet.json").toFile(), HullModificationModel[].class));
        hulls_config.put("viking", mapper.readValue(Paths.get("files\\hulls\\viking.json").toFile(), HullModificationModel[].class));
        hulls_config.put("mamont", mapper.readValue(Paths.get("files\\hulls\\mamont.json").toFile(), HullModificationModel[].class));
        hulls_config.put("hornet_xt", mapper.readValue(Paths.get("files\\hulls\\hornet_xt.json").toFile(), HullModificationModel[].class));
        hulls_config.put("viking_xt", mapper.readValue(Paths.get("files\\hulls\\viking_xt.json").toFile(), HullModificationModel[].class));
        hulls_config.put("ufo", mapper.readValue(Paths.get("files\\hulls\\ufo.json").toFile(), HullModificationModel[].class));
        turrets_config.put("smoky", mapper.readValue(Paths.get("files\\weapons\\smoky.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("flamethrower", mapper.readValue(Paths.get("files\\weapons\\flamethrower.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("twins", mapper.readValue(Paths.get("files\\weapons\\twins.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("railgun", mapper.readValue(Paths.get("files\\weapons\\railgun.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("isida", mapper.readValue(Paths.get("files\\weapons\\isida.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("thunder", mapper.readValue(Paths.get("files\\weapons\\thunder.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("frezee", mapper.readValue(Paths.get("files\\weapons\\frezee.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("ricochet", mapper.readValue(Paths.get("files\\weapons\\ricochet.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("shaft", mapper.readValue(Paths.get("files\\weapons\\shaft.json").toFile(), TurretModificationModel[].class));
        turrets_config.put("snowman", mapper.readValue(Paths.get("files\\weapons\\snowman.json").toFile(), TurretModificationModel[].class));
        System.out.println("[CFG] " + turrets_config.size() + " turrets and " + hulls_config.size() + " hulls loaded.");
    }

    public synchronized static String createBattle(String name, String adminRank, String type, String map, int time, boolean isPaid, int maxPeople, int maxRank, int minRank, int maxScore, boolean autoBalance, boolean ff, boolean noBonus) {
        for (int i = 10000; i < 100000; i++) {
            String battleId = i + "@" + name + "@#" + battles.size();
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

    public synchronized static void createBattle(Battle battle, String adminRank) {
        for (int i = 10000; i < 100000; i++) {
            String battleId = i + "@" + battle.name + "@#" + battles.size();
            if (!battles.containsKey(battleId)) {
                battle.id = battleId;
                battles.put(battle.id, battle);
                battle.start();
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
