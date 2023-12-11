package amalgama.xml.map;

public class Vector3d {
    private float x;
    private float y;

    private float z;

    public Vector3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d() {}

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public String toString() {
        return "x = " + this.x + " y = " + this.y + " z = " + this.z;
    }
}
