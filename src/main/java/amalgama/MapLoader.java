package amalgama;

import amalgama.xml.MapParser;
import amalgama.xml.map.MapModel;

import java.io.File;
import java.util.HashMap;

public class MapLoader {
    public static HashMap<String, MapModel> maps = new HashMap<>();

    public static void loadMaps() {
        try {
            MapParser parser = new MapParser();
            File[] files = new File("files\\maps").listFiles();
            for (File file : files) {
                String fileName = file.getName();
                if (file.isDirectory() || !fileName.endsWith(".xml"))
                    continue;
                MapModel map = parser.parse(file);
                maps.put(fileName.substring(0, fileName.indexOf(".xml")), map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        System.out.println("[MAPS] Maps loaded.");
    }
}
