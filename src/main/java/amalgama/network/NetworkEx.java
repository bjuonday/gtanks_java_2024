package amalgama.network;

public class NetworkEx extends Network implements Runnable {
    private boolean ok = true;
    private StringBuffer in;

    public NetworkEx(Client client) {
        super(client);
    }

    public void readEvent(Command cmd) {
        Handler handler;
        //System.out.println("Receive: " + cmd.src);

        switch (cmd.type) {
            case SYSTEM -> handler = new SystemHandler(this);
            case AUTH -> handler = new AuthHandler(this);
            case LOBBY -> handler = new LobbyHandler(this);
            case GARAGE -> handler = new ProfileHandler(this);
            case LOBBY_CHAT -> handler = new LobbyChatHandler(this);
            case BATTLE -> handler = new BattleHandler(this);
            case PROFILE -> handler = new ProfileHandler(this);
            default -> {
                System.out.println("Unknown type: " + cmd.args[0]);
                return;
            }
        }

        handler.handle(cmd);
    }

    @Override
    public void run() {
        System.out.println("Client '" + stringifySocket() + "' init.");
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
