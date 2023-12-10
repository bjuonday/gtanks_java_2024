package amalgama.network;

import amalgama.database.UserMount;
import amalgama.database.dao.UserMountDAO;
import amalgama.network.netty.TransferProtocol;

public class GarageHandler extends Handler {
    public GarageHandler(TransferProtocol network) {
        super(network);
    }

    @Override
    public void handle(Command command) {
        if (!net.client.authorized)
            return;

        if (command.args[1].equals("get_garage_data")) {
            UserMount mount = UserMountDAO.getMount(net.client.userData.getId());
            if (mount != null) {
                net.send(Type.GARAGE, "init_mounted_item", mount.getWeaponId());
                net.send(Type.GARAGE, "init_mounted_item", mount.getArmorId());
                net.send(Type.GARAGE, "init_mounted_item", mount.getColorId());
            }
        }
    }
}
