package amalgama.network.managers;

import amalgama.Global;
import amalgama.database.dao.UserDAO;
import amalgama.json.lobby.*;
import amalgama.lobby.Battle;
import amalgama.lobby.LobbyMessage;
import amalgama.network.Type;
import amalgama.network.netty.TransferProtocol;
import amalgama.utils.FileUtils;
import amalgama.utils.RandomUtils;
import amalgama.utils.RankUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class LobbyManager {
    private TransferProtocol net;
    private static BattleTypeModel[] battleTypeModel;

    public LobbyManager(TransferProtocol net) {
        this.net = net;
    }

    static {
        try {
            battleTypeModel = new ObjectMapper().readValue(FileUtils.readFile("files/battles.json"), BattleTypeModel[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void initBattles() throws IOException {
        if (net.client.userData == null)
            return;

        ObjectMapper mapper = new ObjectMapper();

        InitBattlesModel battlesModel = new InitBattlesModel();
        battlesModel.haveSubscribe = false;

        battlesModel.items = battleTypeModel;//mapper.readValue(FileUtils.readFile("files/battles.json"), BattleTypeModel[].class);

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

        net.send(Type.LOBBY, "init_battle_select", str);
    }

    public void initChat() throws IOException {
        if (net.client.userData == null)
            return;

        InitChatModel initChatModel = new InitChatModel();
        initChatModel.messages = new ArrayList<>();
        for (LobbyMessage message : Global.lobbyMessages) {
            LobbyMessageModel msg = new LobbyMessageModel();
            msg.name = message.name;
            msg.message = message.text;
            msg.rang = message.rank;
            msg.system = message.isSystem;
            msg.yellow = message.isYellow;
            msg.addressed = message.haveTarget;
            msg.nameTo = message.targetName;
            msg.rangTo = message.targetRank;
            initChatModel.messages.add(msg);
        }

        ObjectMapper mapper = new ObjectMapper();
        net.send(Type.LOBBY_CHAT, "init_chat");
        net.send(Type.LOBBY_CHAT, "init_messages", mapper.writeValueAsString(initChatModel), "[]");
    }

    public void parseCommand(String cmd) {
        String[] args = cmd.trim().split(" ");

        if (args[0].equalsIgnoreCase("addcry") && args.length == 2) {
            if (args[1].isEmpty())
                return;
            try {
                int value = Integer.parseInt(args[1]);
                net.client.userData.setBalance(net.client.userData.getBalance() + value);
                net.send(Type.LOBBY, "add_crystall", String.valueOf(net.client.userData.getBalance()));
                UserDAO.updateUser(net.client.userData);
            } catch (Exception ignored) {}
        }
        else if (args[0].equalsIgnoreCase("addscore") && args.length == 2) {
            if (args[1].isEmpty())
                return;
            try {
                int value = Integer.parseInt(args[1]);
                int rank = RankUtils.getRankFromScore(net.client.userData.getScore());
                net.client.userData.setScore(net.client.userData.getScore() + value);
                int newRank = RankUtils.getRankFromScore(net.client.userData.getScore());
                net.send(Type.LOBBY, "add_score", String.valueOf(net.client.userData.getScore()));

                int prevNextScore = RankUtils.getScoreFromRank(newRank - 1);
                int nextScore = RankUtils.getScoreFromRank(newRank);

                if (newRank != rank)
                    net.send(Type.LOBBY, "update_rang", String.valueOf(newRank), String.valueOf(nextScore));
                net.send(Type.LOBBY, "update_rang_progress", String.valueOf(RankUtils.getRankProgress(net.client.userData.getScore(), nextScore, prevNextScore)));
                UserDAO.updateUser(net.client.userData);
            } catch (Exception ignored) {}
        }
    }
}
