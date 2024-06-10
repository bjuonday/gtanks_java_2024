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

    public static BonusType fromString(String id) {
        if (id.equalsIgnoreCase("crystal"))
            return CRYSTAL;
        else if (id.equalsIgnoreCase("health"))
            return HEALTH;
        else if (id.equalsIgnoreCase("armor"))
            return ARMOR;
        else if (id.equalsIgnoreCase("damage"))
            return DAMAGE;
        else if (id.equalsIgnoreCase("nitro"))
            return NITRO;
        else if (id.equalsIgnoreCase("gold"))
            return GOLD_100;
        else
            return null;
    }
}
