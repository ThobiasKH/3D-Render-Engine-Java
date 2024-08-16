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
}