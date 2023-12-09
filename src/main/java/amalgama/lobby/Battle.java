package amalgama.lobby;

import amalgama.models.MineModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Battle {
    private BattleService service;
    public Map<String, BattleUser> users = new HashMap<>();
    public List<MineModel> mines = new ArrayList<>();
    public String id;
    public String type;
    public String name;
    public String mapId;
    public String previewId;
    public int fund;
    public int redPeople;
    public int bluePeople;
    public int maxPeople;
    public int minRank;
    public int maxRank;
    public int maxScore;
    public int redScore;
    public int blueScore;
    public long startTime;
    public long timeLength;
    public boolean autoBalance;
    public boolean ff;
    public boolean isTeam;
    public boolean isPaid;
    public boolean noBonus;

    public void start() {
        this.service = new BattleService(this);
    }
}
