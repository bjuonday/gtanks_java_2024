package amalgama.battle.weapons;

import amalgama.battle.Weapon;
import amalgama.battle.Tank;
import amalgama.utils.RandomUtils;

public class SmokyWeapon extends Weapon {
    @Override
    public int calculateDamage(Tank target) {
        int minDamage = (int) attacker.turret.min_damage;
        int maxDamage = (int) attacker.turret.max_damage;
        int damage = 0;

        int K = RandomUtils.randomIntBetween(0, 101);
        if (K > 90) {
            damage = RandomUtils.randomIntBetween(maxDamage - 3, maxDamage + 1);
        }
        else {
            int a = Math.abs(maxDamage - minDamage) / 3 + minDamage;
            damage = RandomUtils.randomIntBetween(a, a * 2);
        }

        return (Tank.MAX_HEALTH * damage / target.hull.hp);
    }

    public SmokyWeapon(Tank attacker) {
        super(attacker);
    }
}
