package amalgama.battle;

public enum BonusType {
    HEALTH("medkit", "health"),
    ARMOR("armorup", "armor"),
    DAMAGE("damageup", "damage"),
    NITRO("nitro", "nitro"),
    CRYSTAL("crystal", "crystall"),
    GOLD_100("crystal_100", "gold"),
    GOLD_500("crystal_500", "gold");

    private final String id;
    private final String csid;

    BonusType(String id, String csid) {
        this.id = id;
        this.csid = csid;
    }

    @Override
    public String toString() {
        return id;
    }

    public String toCSString() {
        return csid;
    }
}
