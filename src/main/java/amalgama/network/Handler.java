package amalgama.network;

public abstract class Handler {
    protected Network net = null;
    public Handler(Network network) {
        net = network;
    }
    public abstract void handle(Command command);
}
