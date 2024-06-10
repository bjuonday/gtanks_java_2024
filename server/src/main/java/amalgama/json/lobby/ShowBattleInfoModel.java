package amalgama.json.lobby;

import java.util.List;

public class ShowBattleInfoModel {
    public List<UserInBattleModel> users_in_battle;
    public String name;
    public String type;
    public String battleId;
    public String previewId;
    public int maxPeople;
    public int minRank;
    public int maxRank;
    public int timeLimit;
    public int timeCurrent;
    public int killsLimit;
    public int scoreRed;
    public int scoreBlue;
    public boolean autobalance;
    public boolean frielndyFie;
    public boolean paidBattle;
    public boolean withoutBonuses;
    public boolean userAlreadyPaid;
    public boolean fullCash;
    public boolean spectator;
}
