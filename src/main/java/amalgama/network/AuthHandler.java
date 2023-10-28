package amalgama.network;

import amalgama.database.User;
import amalgama.database.dao.UserDAO;

public class AuthHandler extends Handler {

    public AuthHandler(Network network) {
        super(network);
    }

    @Override
    public void handle(Command command) {
        if (command.args.length != 3)
            return;

        try {
            User user = UserDAO.getUser(command.args[1], command.args[2]);
            if (user != null) {
                net.send(Type.AUTH, "accept");
                return;
            }
            net.send(Type.AUTH, "denied");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
