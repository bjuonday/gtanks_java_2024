package amalgama.network;

public class NetworkEx extends Network implements Runnable {
    private boolean ok = true;
    private StringBuffer in;

    public NetworkEx(Client client) {
        super(client);
    }

    public void readEvent(Command cmd) {
        Handler handler = null;

        switch (cmd.type) {
            case SYSTEM:
                handler = new SystemHandler(this);
                break;
            case AUTH:
                handler = new AuthHandler(this);
                break;
            case LOBBY:
                handler = new LobbyHandler(this);
                break;
            case LOBBY_CHAT:
                handler = new LobbyChatHandler(this);
                break;
            case BATTLE:
                handler = new BattleHandler(this);
                break;
            case PROFILE:
                handler = new ProfileHandler(this);
                break;
            case UNKNOWN:
            default:
                System.out.println("Unknown type: " + cmd.args[0]);
                return;
        }

        assert handler != null;
        handler.handle(cmd);
    }

    @Override
    public void run() {
        try {
            while (ok && nBytes != -1) {
                in = new StringBuffer(read().trim());
                if (in.isEmpty())
                    continue;

                int pos = in.toString().indexOf(DELIMETER);
                if (pos == -1)
                    continue;;

                in.setLength(pos);
                readEvent(new Command(decrypt(in.toString())));
            }
        } catch (Exception e) {
            ok = false;
            e.printStackTrace();
        }
    }
}
