package amalgama.battle;

import amalgama.battle.weapons.*;

public class WeaponFactory {

    public static Weapon getWeapon(Tank attacker) {
        if (attacker.mount.getWeaponId().startsWith("smoky_m"))
            return new SmokyWeapon(attacker);
        return null;
    }
}
