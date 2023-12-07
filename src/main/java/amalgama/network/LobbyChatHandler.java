package amalgama.network;

import amalgama.Global;
import amalgama.json.lobby.LobbyMessageModel;
import amalgama.utils.RankUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class LobbyChatHandler extends Handler {
    ObjectMapper mapper;
    public LobbyChatHandler(Network network) {
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
            if (command.args.length == 4) {
                String text = command.args[1];
                String target = command.args[3];
                String author = net.client.userData.getLogin();
                int rank = RankUtils.getRankFromScore(net.client.userData.getScore());

                boolean addressed = !target.equalsIgnoreCase("NULL");

                if (addressed) {
                    var user = Global.clients.get(target);
                    if (user != null) {
                        int targetRank = RankUtils.getRankFromScore(user.userData.getScore());
                        LobbyMessageModel model = Global.putLobbyMessage(author, text, rank, true, target, targetRank);
                        Network.broadcast_Lobby(Type.LOBBY_CHAT, mapper.writeValueAsString(model));
                        return;
                    }
                }
                LobbyMessageModel model = Global.putLobbyMessage(author, text, rank, false, null, 0);
                Network.broadcast_Lobby(Type.LOBBY_CHAT, mapper.writeValueAsString(model));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}