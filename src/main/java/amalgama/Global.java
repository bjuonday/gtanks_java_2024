package amalgama;

import amalgama.lobby.Battle;
import amalgama.lobby.LobbyMessage;
import amalgama.models.RankModel;
import amalgama.network.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Global {
    public static Map<String, Client> clients = new HashMap<>();
    public static List<RankModel> ranks;
    public static Map<String, Battle> battles = new HashMap<>();
    public static List<LobbyMessage> lobbyMessages = new ArrayList<>();

    public static void createBattle(String name, String adminRank, String type, String map, int time, boolean isPaid, int maxPeople, int maxRank, int minRank, int maxScore, boolean autoBalance, boolean ff, boolean noBonus) {
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
                battle.isTeam = !type.equals("DM");
                battle.noBonus = noBonus;
                battle.redPeople = 0;
                battle.bluePeople = 0;
                battle.fund = 0;
                battles.put(battle.id, battle);
                //battle.start();
                return;
            }
        }
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
}
