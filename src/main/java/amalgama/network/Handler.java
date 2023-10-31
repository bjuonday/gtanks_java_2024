package amalgama.network;

import amalgama.network.managers.LobbyManager;

public abstract class Handler {
    protected Network net = null;
    protected LobbyManager lobbyManager = null;
    public Handler(Network network) {
        net = network;
        lobbyManager = new LobbyManager(net);
    }
    public abstract void handle(Command command);
}
