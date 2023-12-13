package amalgama.network;

import amalgama.database.User;

public class Client {
    public User userData = null;
    public boolean authorized = false;
    public boolean encrypted = false;
    public String currentBattleId = null;
    public float scoreBonusPercent = 2.0f;  // 1.5 = +50%, 1.3 = +30%, 0.7 = -30%
}
