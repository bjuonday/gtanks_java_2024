package amalgama.network;

import amalgama.Global;
import amalgama.database.UserMount;
import amalgama.database.dao.UserMountDAO;
import amalgama.network.managers.GarageManager;
import amalgama.network.netty.TransferProtocol;
import amalgama.network.secure.Grade;
import org.json.simple.JSONObject;

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
            if (mount != null) {//init_mounted_item mount_item
                net.send(Type.GARAGE, "init_mounted_item", mount.getWeaponId());
                net.send(Type.GARAGE, "init_mounted_item", mount.getArmorId());
                net.send(Type.GARAGE, "init_mounted_item", mount.getColorId());
            }
        }
        else if (command.args[1].equals("try_buy_item") && command.args.length >= 3) {
            String itemId = command.args[2];
            int count = command.args.length == 4 ? Integer.parseInt(command.args[3]) : 1;
            int newBalance = GarageManager.buyItem(net.client.userData, itemId, count);
            if (newBalance >= 0) {
                for (var item : Global.garageItems) {
                    if (item.id.equals(itemId.substring(0, itemId.length() - 3))) {
                        JSONObject json = new JSONObject();
                        json.put("count", count);
                        json.put("addable", item.type == 4);
                        json.put("multicounted", item.type == 4);
                        net.send(Type.LOBBY, "add_crystall", String.valueOf(newBalance));
                        net.send(Type.GARAGE, "buy_item", itemId, json.toJSONString());
                        break;
                    }
                }
            }
            else if (newBalance == -2) {
                net.vrs.registerAct("attempt_buy_unknown_item", Grade.DANGEROUS);
            }
        }
        else if (command.args[1].equals("try_mount_item") && command.args.length == 3) {
            String itemId = command.args[2];
            if (GarageManager.mountItem(net.client.userData, itemId)) {
                net.send(Type.GARAGE, "mount_item", itemId);
            }
            else {
                net.vrs.registerAct("attempt_mount_noaccess_item", Grade.DANGEROUS);
            }
        }
        else if (command.args[1].equals("try_update_item") && command.args.length == 3) {
            String itemId = command.args[2];
            int newBalance = GarageManager.upgradeItem(net.client.userData, itemId);
            if (newBalance >= 0) {
                net.send(Type.LOBBY, "add_crystall", String.valueOf(newBalance));
                net.send(Type.GARAGE, "update_item", itemId);
            }
            else {
                net.vrs.registerAct("attempt_upgrade_unavailable_item", Grade.DETRIMENTAL);
            }
        }
    }
}
