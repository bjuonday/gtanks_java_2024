package amalgama.network;

import amalgama.network.managers.LobbyManager;
import amalgama.network.netty.TransferProtocol;

public abstract class Handler {
    protected TransferProtocol net;
    protected LobbyManager lobbyManager = null;
    public Handler(TransferProtocol network) {
        net = network;
        lobbyManager = new LobbyManager(net);
    }
    public abstract void handle(Command command);
}
