package amalgama.utils;

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
}
