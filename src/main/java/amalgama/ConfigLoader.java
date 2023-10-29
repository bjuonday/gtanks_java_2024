package amalgama;


import amalgama.utils.XmlUtils;

public class ConfigLoader {
    public static void loadRanks() {
        Global.ranks = XmlUtils.getRanks();
    }
}
