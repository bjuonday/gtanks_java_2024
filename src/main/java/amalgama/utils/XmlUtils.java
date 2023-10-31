package amalgama.utils;

import amalgama.lobby.Battle;
import amalgama.models.RankModel;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {
    public static List<RankModel> getRanks() {
        List<RankModel> list = new ArrayList<>();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/files/config/ranks.xml"));

            NodeList list_ranks = document.getElementsByTagName("rank");

            for (int i = 0; i < list_ranks.getLength(); i++) {
                Node node_rank = list_ranks.item(i);
                NamedNodeMap map_attributes = node_rank.getAttributes();
                Node attr_score = map_attributes.getNamedItem("score");
                int score = Integer.parseInt(attr_score.getNodeValue());
                RankModel rank = new RankModel();
                rank.score = score;
                list.add(rank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Battle> getBattles() {
        List<Battle> list = new ArrayList<>();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/files/config/startup_battles.xml"));

            NodeList list_battles = document.getElementsByTagName("battle");

            for (int i = 0; i < list_battles.getLength(); i++) {
                Node node_battle = list_battles.item(i);
                NamedNodeMap map_attributes = node_battle.getAttributes();

                Battle battle = new Battle();

                battle.name = map_attributes.getNamedItem("name").getNodeValue();
                battle.mapId = map_attributes.getNamedItem("map").getNodeValue();
                battle.previewId = battle.mapId + "_preview";
                battle.type = map_attributes.getNamedItem("type").getNodeValue();
                battle.maxPeople = Integer.parseInt(map_attributes.getNamedItem("max").getNodeValue());
                battle.minRank = Integer.parseInt(map_attributes.getNamedItem("min-rank").getNodeValue());
                battle.maxRank = Integer.parseInt(map_attributes.getNamedItem("max-rank").getNodeValue());
                battle.maxScore = Integer.parseInt(map_attributes.getNamedItem("max-score").getNodeValue());
                battle.timeLength = Integer.parseInt(map_attributes.getNamedItem("time").getNodeValue());
                battle.isPaid = Boolean.parseBoolean(map_attributes.getNamedItem("paid").getNodeValue());
                battle.noBonus = Boolean.parseBoolean(map_attributes.getNamedItem("no-bonus").getNodeValue());
                battle.ff = Boolean.parseBoolean(map_attributes.getNamedItem("ff").getNodeValue());
                battle.autoBalance = Boolean.parseBoolean(map_attributes.getNamedItem("auto-balance").getNodeValue());

                list.add(battle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
