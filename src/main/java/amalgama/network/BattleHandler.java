package amalgama.network;

import amalgama.network.netty.TransferProtocol;

public class BattleHandler extends Handler {
    public BattleHandler(TransferProtocol network) {
        super(network);
    }

    @Override
    public void handle(Command command) {

    }
}
