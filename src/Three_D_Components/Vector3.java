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

    public static Vector3 applyProjectionMatrix(Vector3 inputVector, Mat4x4 transformationMatrix) {
        float inputX = inputVector.getX();
        float inputY = inputVector.getY();
        float inputZ = inputVector.getZ();

        float resultX = inputX * transformationMatrix.get(0, 0) + inputY * transformationMatrix.get(1, 0) + inputZ * transformationMatrix.get(2, 0) + transformationMatrix.get(3, 0);
        float resultY = inputX * transformationMatrix.get(0, 1) + inputY * transformationMatrix.get(1, 1) + inputZ * transformationMatrix.get(2, 1) + transformationMatrix.get(3, 1);
        float resultZ = inputX * transformationMatrix.get(0, 2) + inputY * transformationMatrix.get(1, 2) + inputZ * transformationMatrix.get(2, 2) + transformationMatrix.get(3, 2);
        float w       = inputX * transformationMatrix.get(0, 3) + inputY * transformationMatrix.get(1, 3) + inputZ * transformationMatrix.get(2, 3) + transformationMatrix.get(3, 3);
        
        if (w != 0f) {
            resultX /= w;
            resultY /= w;
            resultZ /= w;
        }

        return new Vector3(resultX, resultY, resultZ);
    }

    public void rotateAroundXAxisWithPoint(float angleInRadians, Vector3 point) {
        this.x -= point.getX();
        this.y -= point.getY();
        this.z -= point.getZ();

        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        
        float newY = this.y * cosAngle - this.z * sinAngle;
        float newZ = this.y * sinAngle + this.z * cosAngle;
        
        this.y = newY;
        this.z = newZ;

        this.x += point.getX();
        this.y += point.getY();
        this.z += point.getZ();
    }

    public void rotateAroundYAxisWithPoint(float angleInRadians, Vector3 point) {
        this.x -= point.getX();
        this.y -= point.getY();
        this.z -= point.getZ();

        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        
        float newX = this.x * cosAngle + this.z * sinAngle;
        float newZ = -this.x * sinAngle + this.z * cosAngle;
        
        this.x = newX;
        this.z = newZ;

        this.x += point.getX();
        this.y += point.getY();
        this.z += point.getZ();
    }

    public void rotateAroundZAxisWithPoint(float angleInRadians, Vector3 point) {
        this.x -= point.getX();
        this.y -= point.getY();
        this.z -= point.getZ();

        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        
        float newX = this.x * cosAngle - this.y * sinAngle;
        float newY = this.x * sinAngle + this.y * cosAngle;
        
        this.x = newX;
        this.y = newY;

        this.x += point.getX();
        this.y += point.getY();
        this.z += point.getZ();
    }

    public static Vector3 cross(Vector3 v0, Vector3 v1) {
        float x0 = v0.getX();
        float y0 = v0.getY();
        float z0 = v0.getZ();

        float x1 = v1.getX();
        float y1 = v1.getY();
        float z1 = v1.getZ();

        float nX = y0 * z1 - z0 * y1;
        float nY = z0 * x1 - x0 * z1;
        float nZ = x0 * y1 - y0 * x1;

        return new Vector3(nX, nY, nZ);
    }

    public static float dot(Vector3 vo0, Vector3 vo1) {
        Vector3 v0 = Vector3.clone(vo0);
        Vector3 v1 = Vector3.clone(vo1);

        v0 = Vector3.getNormalized(v0);
        v1 = Vector3.getNormalized(v1);

        return v0.getX() * v1.getX() + v0.getY() * v1.getY() + v0.getZ() * v1.getZ();
    }

    public static Vector3 getNormalized(Vector3 vector) {
        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();

        float l = Vector3.getLength(vector);

        x /= l;
        y /= l;
        z /= l;

        return new Vector3(x, y, z);
    }

    public static float getLength(Vector3 vector) {
        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();
        float length = (float) Math.sqrt(x * x + y * y + z * z);
        return length;
    }

    public static Vector3 clone(Vector3 vectorToBeCloned) {
        Vector3 result = new Vector3(vectorToBeCloned.getX(), vectorToBeCloned.getY(), vectorToBeCloned.getZ());
        return result;
    }

    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vector3 subtract(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    public static Vector3 multiply(Vector3 v, float scalar) {
        return new Vector3(v.getX() * scalar, v.getY() * scalar, v.getZ() * scalar);
    }

    public static Vector3 divide(Vector3 v, float scalar) {
        if (scalar == 0) throw new IllegalArgumentException("Scalar value cannot be zero.");
        return new Vector3(v.getX() / scalar, v.getY() / scalar, v.getZ() / scalar);
    }
}