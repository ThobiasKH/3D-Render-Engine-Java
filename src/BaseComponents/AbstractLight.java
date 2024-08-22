package BaseComponents;

public class AbstractLight {
    private float intensity;
    private Vector3 direction;

    public AbstractLight(float intensity, Vector3 direction) {
        this.intensity = Math.clamp(intensity, 0, 1);
        this.direction = Vector3.getNormalized(direction);
    }

    public float getIntensity() {return this.intensity;}
    public void setIntensity(int intensity) {this.intensity = intensity;}

    public Vector3 getDirection() {return this.direction;}
    public void setDirection(Vector3 dir) {this.direction = dir;}

    public void rotateAroundXAxis(float angleInRadians) {
        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);

        float y = this.direction.getY();
        float z = this.direction.getZ();
        
        float newY = y * cosAngle - z * sinAngle;
        float newZ = y * sinAngle + z * cosAngle;
        
        this.direction.setY(newY);
        this.direction.setZ(newZ);
    }

    public void rotateAroundYAxis(float angleInRadians) {
        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        
        float x = this.direction.getX();
        float z = this.direction.getZ();
    
        float newX = x * cosAngle + z * sinAngle;
        float newZ = x * sinAngle + z * cosAngle;
        
        this.direction.setX(newX);
        this.direction.setZ(newZ);
    }

    public void rotateAroundZAxis(float angleInRadians) {
        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        
        float x = this.direction.getX();
        float y = this.direction.getY();

        float newX = x * cosAngle - y * sinAngle;
        float newY = x * sinAngle + y * cosAngle;
        
        this.direction.setX(newX);
        this.direction.setY(newY);
    }
}
