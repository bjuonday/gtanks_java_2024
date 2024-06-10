package amalgama.battle;

public abstract class Weapon {
    protected final Tank attacker;

    public abstract int calculateDamage(Tank target);

    public Weapon(Tank attacker) {
        this.attacker = attacker;
    }
}
