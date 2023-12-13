package amalgama.battle;

import amalgama.models.MapPointModel;

public class Bonus {
    public MapPointModel position;
    public BonusType type;
    public long spawnTime;

    public Bonus(MapPointModel position, BonusType type) {
        this.position = position;
        this.type = type;
        spawnTime = System.currentTimeMillis();
    }
}
