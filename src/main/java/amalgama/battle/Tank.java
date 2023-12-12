package amalgama.battle;

import amalgama.database.UserMount;
import amalgama.models.HullModificationModel;
import amalgama.models.TurretModificationModel;

public class Tank {
    public static final int MAX_HEALTH = 10000;
    public String nickname;
    public TurretModificationModel turret;
    public HullModificationModel hull;
    public final Weapon weapon;
    public SpawnState spawnState;
    public int health;
    public UserMount mount;

    public Tank(String nickname, TurretModificationModel turret, HullModificationModel hull, UserMount mount) {
        this.nickname = nickname;
        this.turret = turret;
        this.hull = hull;
        this.mount = mount;
        this.weapon = WeaponFactory.getWeapon(this);
        this.spawnState = SpawnState.STATE_DEAD;
    }
}
