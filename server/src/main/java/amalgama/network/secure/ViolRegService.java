package amalgama.network.secure;

import amalgama.network.netty.TransferProtocol;


// Violation Registration Service
public class ViolRegService {
    private final TransferProtocol protocol;
    private int trustFactor;
    private long timePointLastCreation;
    private int activeBattles;
    private int intervalBattles;

    public ViolRegService(TransferProtocol protocol) {
        this.protocol = protocol;
        trustFactor = 1000;
        timePointLastCreation = 0L;
        activeBattles = 0;
        intervalBattles = 0;
    }

    private void addTrust(int value) {
        trustFactor = Math.min(1000, Math.max(0, trustFactor + value));
        if (trustFactor == 0) {
            protocol.close();
        }
    }

    public void registerAct(String name, Grade grade) {
        switch (grade) {
            case SAFE -> addTrust(0);
            case SUSPICIOUS -> addTrust(-100);
            case DETRIMENTAL -> addTrust(-300);
            case DANGEROUS -> addTrust(-700);
            case DESTRUCTIVE -> addTrust(-1000);
            default -> {
            }
        }
        System.out.println("[VRS] (" + grade.name() + ") '" + name + "' registered.");
    }

    public boolean tryCreateBattle() {
        if (activeBattles >= Limits.MAX_BATTLES) {
            registerAct("battle_spam", Grade.SUSPICIOUS);
            return false;
        }

        if (intervalBattles == 0) {
            intervalBattles = 1;
            activeBattles++;
            timePointLastCreation = System.currentTimeMillis();
            return true;
        }
        else if (intervalBattles < Limits.MAX_BATTLES_IN_INTERVAL) {
            intervalBattles++;
            return true;
        }
        else {
            long currTime = System.currentTimeMillis();
            if ((currTime - timePointLastCreation) < (Limits.BATTLES_INTERVAL * 1000)) {
                registerAct("battle_spam", Grade.SUSPICIOUS);
                return false;
            }
            activeBattles++;
            intervalBattles = 1;
            return true;
        }
    }

    public void removeBattle() {
        if (activeBattles > 0)
            activeBattles--;
    }
}
