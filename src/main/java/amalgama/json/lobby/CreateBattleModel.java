package amalgama.json.lobby;

public class CreateBattleModel {
    public String battleId;
    public String mapId;
    public String name;
    public boolean team;
    public int redPeople;
    public int bluePeople;
    public int countPeople; // current peoples ( for team sum of red + blue )
    public int maxPeople; // max available
    public int minRank;
    public int maxRank;
    public boolean isPaid;
}
