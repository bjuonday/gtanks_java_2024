package amalgama.xml.map.bonus;

public class BonusTypeModel {
    public static final BonusTypeModel NITRO = new BonusTypeModel("nitro");
    public static final BonusTypeModel DD = new BonusTypeModel("damageup");
    public static final BonusTypeModel DA = new BonusTypeModel("armorup");
    public static final BonusTypeModel HEALTH = new BonusTypeModel("medkit");
    public static final BonusTypeModel CRYSTAL = new BonusTypeModel("crystal");
    public static final BonusTypeModel GOLD_100 = new BonusTypeModel("crystal_100");
    public static final BonusTypeModel GOLD_500 = new BonusTypeModel("crystal_500");
    private final String value;

    public BonusTypeModel() {
        value = "";
    }

    public BonusTypeModel(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }
}
