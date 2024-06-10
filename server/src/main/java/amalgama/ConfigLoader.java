package amalgama;


import amalgama.lobby.Battle;
import amalgama.utils.XmlUtils;

public class ConfigLoader {
    public static void loadRanks() {
        Global.ranks = XmlUtils.getRanks();
    }

    public static void loadStartup() {
        MapLoader.loadMaps();
        for (Battle battle : XmlUtils.getBattles())
            Global.createBattle(battle, "user");
        Global.init();
    }
}
