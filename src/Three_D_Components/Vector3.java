package Three_D_Components;

public class Vector3 {
    private float x;
    private float y;
    private float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setZ(float z) { this.z = z; }

    public static Vector3 clone(Vector3 vectorToBeCloned) {
        Vector3 result = new Vector3(vectorToBeCloned.getX(), vectorToBeCloned.getY(), vectorToBeCloned.getZ());
        return result;
    }
}