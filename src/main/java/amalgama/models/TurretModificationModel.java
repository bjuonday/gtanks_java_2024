package amalgama.models;

public class TurretModificationModel implements Cloneable {
    public String modification;
    public int charingTime;
    public int weakeningCoeff;
    public int numRaysDown;
    public int numRaysUp;
    public int reloadMsec;
    public int target_detection_interval;
    public int heating_speed;
    public int cooling_speed;
    public int heat_limit;
    public int capacity;
    public int chargeRate;
    public int dischargeRate;
    public int energyCapacity;
    public int energyDischargeSpeed;
    public int energyRechargeSpeed;
    public int weaponTickMsec;
    public int tickPeriod;
    public int energyPerShot;
    public int charge_rate;
    public int discharge_rate;
    public int inital_fov;
    public int minimum_fov;
    public double min_damage;
    public double max_damage;
    public double autoAimingAngleDown;
    public double autoAimingAngleUp;
    public double impactCoeff;
    public double kickback;
    public double turretRotationAccel;
    public double turretRotationSpeed;
    public double max_damage_radius;
    public double min_damage_radius;
    public double min_damage_percent;
    public double range;
    public double cone_angle;
    public double shot_range;
    public double shot_radius;
    public double shot_speed;
    public double lockAngle;
    public double lockAngleCos;
    public double maxAngle;
    public double maxAngleCos;
    public double maxRadius;
    public double maxSplashDamageRadius;
    public double minSplashDamageRadius;
    public double minSplashDamagePercent;
    public double impactForce;
    public double damageAreaConeAngle;
    public double damageAreaRange;
    public double coolingSpeed;
    public double shotRadius;
    public double shotSpeed;
    public double shotDistance;
    public double frezee_speed;
    public double max_energy;
    public double elevation_angle_up;
    public double elevation_angle_down;
    public double vertical_targeting_speed;
    public double horizontal_targeting_speed;
    public double shrubs_hiding_radius_min;
    public double shrubs_hiding_radius_max;
    public double impact_quick_shot;

    @Override
    public TurretModificationModel clone() throws CloneNotSupportedException {
        return (TurretModificationModel) super.clone();
    }
}
