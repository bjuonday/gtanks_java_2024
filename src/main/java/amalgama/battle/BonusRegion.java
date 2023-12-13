package amalgama.battle;

import amalgama.models.MapPointModel;

public class BonusRegion {
    public MapPointModel max;
    public MapPointModel min;
    public String type;

    public BonusRegion(MapPointModel max, MapPointModel min, String types) {
        this.max = max;
        this.min = min;
        this.type = types;
    }

    @Override
    public String toString() {
        return "BonusRegion{" +
                "max=" + max +
                ", min=" + min +
                ", type='" + type + '\'' +
                '}';
    }
}
