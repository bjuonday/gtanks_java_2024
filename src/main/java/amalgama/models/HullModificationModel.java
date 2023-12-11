package amalgama.models;

public class HullModificationModel implements Cloneable {
    public String modification;
    public double mass;
    public double power;
    public double speed;
    public double turn_speed;
    public int hp;

    @Override
    public HullModificationModel clone() throws CloneNotSupportedException {
        return (HullModificationModel) super.clone();
    }
}
