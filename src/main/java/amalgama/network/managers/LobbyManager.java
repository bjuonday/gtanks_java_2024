package amalgama.network.managers;

import amalgama.Global;
import amalgama.json.BattleModel;
import amalgama.json.BattleTypeModel;
import amalgama.json.InitBattlesModel;
import amalgama.json.InitPanelModel;
import amalgama.lobby.Battle;
import amalgama.lobby.LobbyMessage;
import amalgama.network.Network;
import amalgama.network.Type;
import amalgama.utils.RandomUtils;
import amalgama.utils.RankUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class LobbyManager {
    private Network net;

    public LobbyManager(Network net) {
        this.net = net;
    }

    public void initPanel() throws IOException {
        if (net.client.userData == null)
            return;

        InitPanelModel panel = new InitPanelModel();

        panel.score = net.client.userData.getScore();
        panel.name = net.client.userData.getLogin();
        panel.tester = false;
        panel.rating = 1;
        panel.crystall = net.client.userData.getBalance();
        panel.rang = RankUtils.getRankFromScore(panel.score);
        panel.next_score = RankUtils.getScoreFromRank(panel.rang);
        panel.place = 0;
        panel.email = net.client.userData.getEmail();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(panel);

        net.send(Type.LOBBY, "init_panel", json);
    }

    public void updateRankProgress() throws IOException {
        if (net.client.userData == null)
            return;

        int score = net.client.userData.getScore();
        int rank = RankUtils.getRankFromScore(score);
        int prevNextScore = RankUtils.getScoreFromRank(rank - 1);
        int nextScore = RankUtils.getScoreFromRank(rank);

        net.send(Type.LOBBY, "update_rang_progress", String.valueOf(RankUtils.getRankProgress(score, nextScore, prevNextScore)));
    }

    public void initEffectModel() throws IOException {
        if (net.client.userData == null)
            return;

        net.send(Type.LOBBY, "init_effect_model", "{\"effects\":[]}");
    }

    public void initBattles() throws IOException, ParseException {
        if (net.client.userData == null)
            return;

        ObjectMapper mapper = new ObjectMapper();

        InitBattlesModel battlesModel = new InitBattlesModel();
        battlesModel.haveSubscribe = false;

        battlesModel.items = mapper.readValue(Paths.get("src/main/resources/files/battle_types.json").toFile(), BattleTypeModel[].class);

        battlesModel.battles = new ArrayList<>();

        for (Battle b : Global.battles.values()) {
            BattleModel battle = new BattleModel();
            battle.team = b.isTeam;
            battle.isPaid = b.isPaid;
            battle.redPeople = b.redPeople;
            battle.bluePeople = b.bluePeople;
            battle.countPeople = b.users.size();
            battle.maxPeople = b.maxPeople;
            battle.minRank = b.minRank;
            battle.maxRank = b.maxRank;
            battle.battleId = b.id;
            battle.type = b.type;
            battle.name = b.name;
            battle.mapId = b.mapId;
            battle.previewId = b.previewId;
            battlesModel.battles.add(battle);
        }

        if (battlesModel.battles.size() > 0)
            battlesModel.recommendedBattle = RandomUtils.randomFromList(battlesModel.battles).battleId;

        String str = mapper.writeValueAsString(battlesModel);

        System.out.println(str.substring(100, 150));

        net.send(Type.LOBBY, "init_battle_select", str);
    }

    public void initChat() throws IOException {
        if (net.client.userData == null)
            return;

        JSONObject json = new JSONObject();
        JSONArray messages = new JSONArray();
        for (LobbyMessage message : Global.lobbyMessages) {
            JSONObject msg = new JSONObject();
            msg.put("name", message.name);
            msg.put("message", message.text);
            msg.put("rang", message.rank);
            msg.put("system", message.isSystem);
            msg.put("yellow", message.isYellow);
            msg.put("addressed", message.haveTarget);
            msg.put("nameTo", message.targetName);
            msg.put("rangTo", message.targetRank);
            messages.add(msg);
        }
        json.put("messages", messages);

        net.send(Type.LOBBY_CHAT, "init_chat");
        net.send(Type.LOBBY_CHAT, "init_messages", json.toJSONString(), "[]");
    }
}
