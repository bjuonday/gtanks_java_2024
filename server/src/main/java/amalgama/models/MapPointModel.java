package amalgama.models;

public class MapPointModel implements Cloneable {
    public double x;
    public double y;
    public double z;
    public double a;

    @Override
    public MapPointModel clone() throws CloneNotSupportedException {
        return (MapPointModel) super.clone();
    }

    @Override
    public String toString() {
        return "MapPointModel{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", a=" + a +
                '}';
    }
}
