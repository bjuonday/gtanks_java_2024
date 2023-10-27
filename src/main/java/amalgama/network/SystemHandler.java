package amalgama.network;

import amalgama.utils.FileUtils;


public class SystemHandler extends Handler {

    public SystemHandler(Network network) {
        super(network);
    }

    @Override
    public void handle(Command command) {
        if (command.args.length < 2)
            return;

        try {
            if (command.args[1].equalsIgnoreCase("get_aes_data")) {
                byte[] swf = FileUtils.GetFileBytes("src/main/resources/files/2.swf");
                net.KEY = 2;
                String[] bytes = new String[swf.length];
                for (int i = 0; i < bytes.length; i++)
                    bytes[i] = String.valueOf(swf[i]);
                String sendData = String.join(",", bytes);
                net.send(Type.SYSTEM, "set_aes_data", sendData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
