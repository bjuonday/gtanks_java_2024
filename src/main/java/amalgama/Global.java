package amalgama;

import amalgama.models.RankModel;
import amalgama.network.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Global {
    public static Map<String, Client> clients = new HashMap<>();
    public static List<RankModel> ranks;
}
