package amalgama.network;

public abstract class Handler {
    Network net = null;
    public Handler(Network network) {
        net = network;
    }
    public abstract void handle(Command command);
}
