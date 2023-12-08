package amalgama.network;

import amalgama.network.netty.TransferProtocol;

public class GarageHandler extends Handler {
    public GarageHandler(TransferProtocol network) {
        super(network);
    }

    @Override
    public void handle(Command command) {

    }
}
