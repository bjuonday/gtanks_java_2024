package amalgama.network;

import amalgama.network.netty.TransferProtocol;
import amalgama.utils.FileUtils;


public class SystemHandler extends Handler {

    public SystemHandler(TransferProtocol network) {
        super(network);
    }

    @Override
    public void handle(Command command) {
        if (command.args.length < 2)
            return;

        try {
            if (command.args[1].equalsIgnoreCase("get_aes_data")) {
                byte[] swf = FileUtils.GetFileBytes("files/fixed.swf");
                String[] bytes = new String[swf.length];
                for (int i = 0; i < bytes.length; i++)
                    bytes[i] = String.valueOf(swf[i]);
                String sendData = String.join(",", bytes);
                net.send(Type.SYSTEM, "set_aes_data", sendData);
                net.client.encrypted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
