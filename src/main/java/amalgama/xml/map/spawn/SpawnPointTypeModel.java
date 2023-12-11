package amalgama.xml.map.spawn;

public class SpawnPointTypeModel {
    public static final SpawnPointTypeModel BLUE = new SpawnPointTypeModel();

    public static final SpawnPointTypeModel RED = new SpawnPointTypeModel();

    public static final SpawnPointTypeModel NONE = new SpawnPointTypeModel();

    public static SpawnPointTypeModel getType(String value) {
        if (value.equals("blue"))
            return BLUE;
        if (value.equals("red"))
            return RED;
        if (value.equals("dm"))
            return NONE;
        return NONE;
    }
}
