package amalgama.network;

import amalgama.Global;
import amalgama.database.User;
import amalgama.database.dao.GroupDAO;
import amalgama.database.dao.UserDAO;
import amalgama.network.netty.TransferProtocol;
import amalgama.utils.CryptoHashUtils;


public class AuthHandler extends Handler {

    public AuthHandler(TransferProtocol network) {
        super(network);
    }

    @Override
    public void handle(Command command) {
        if (command.args.length < 3)
            return;

        if (net.client.authorized)
            return;

        try {
            if (command.type == Type.AUTH && command.args.length == 3) {
                User user = UserDAO.getUser(command.args[1], command.args[2]);
                if (user == null) {
                    net.send(Type.AUTH, "denied");
                    return;
                }
                net.send(Type.AUTH, "accept");
                Global.clients.put(user.getLogin(), net.client);
                net.client.authorized = true;
                net.client.userData = user;
            }
            else if (command.type == Type.REGISTRATION) {
                if (command.args[1].equals("check_name")) {
                    if (command.args[2].length() > 3 && UserDAO.getUser(command.args[2]) == null)
                        net.send(Type.REGISTRATION, "check_name_result;not_exist");
                    else
                        net.send(Type.REGISTRATION, "check_name_result;invalid");
                    return;
                }
                else {
                    if (command.args[1].length() < 4)
                        return;

                    User user = new User();
                    user.setLogin(command.args[1]);
                    user.setPassword(CryptoHashUtils.hash(command.args[2], "MD5"));
                    user.setGroup(GroupDAO.getGroup(1L));
                    UserDAO.addUser(user);
                    Global.clients.put(user.getLogin(), net.client);
                    net.client.authorized = true;
                    net.send(Type.REGISTRATION, "info_done");
                    net.client.userData = user;
                }
            }

            lobbyManager.initPanel();
            lobbyManager.updateRankProgress();
            lobbyManager.initEffectModel();
            lobbyManager.initBattles();
            lobbyManager.initChat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
