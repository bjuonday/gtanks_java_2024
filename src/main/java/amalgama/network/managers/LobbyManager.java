package amalgama.network.managers;

import amalgama.network.Network;
import amalgama.network.Type;
import amalgama.utils.RankUtils;
import org.json.simple.JSONObject;

import java.io.IOException;

public class LobbyManager {
    public static void initPanel(Network net) throws IOException {
        if (net.client.userData == null)
            return;

        int score = net.client.userData.getScore();
        String name = net.client.userData.getLogin();
        boolean tester = false;
        int rating = 1;
        int balance = net.client.userData.getBalance();
        int rank = RankUtils.getRankFromScore(score);
        int nextScore = RankUtils.getScoreFromRank(rank);
        int place = 0;
        String email = net.client.userData.getEmail();

        JSONObject json = new JSONObject();
        json.put("score", score);
        json.put("name", name);
        json.put("tester", tester);
        json.put("rating", rating);
        json.put("crystall", balance);
        json.put("next_score", nextScore);
        json.put("place", place);
        json.put("rang", rank);
        json.put("email", email);

        net.send(Type.LOBBY, "init_panel", json.toJSONString());
    }
}
