package RenderPipeline;
import BaseComponents.*;
public class Camera {
    private static Vector3 position = new Vector3(0, 0, 0);

    private static Vector3 forward = new Vector3(0, 0, 1);
    private static Vector3 right = new Vector3(1, 0, 0);
    private static Vector3 up = new Vector3(0, 1, 0);

    private static Mat4x4 lookAt = new Mat4x4();
    private static Mat4x4 lookAtInverse = new Mat4x4();

    private static float zNear = 1f;
    private static float zFar = 1000f;
    private static float fov = 90f; // In degrees 
    private static float aspectRatio = .5f;
    private final static Mat4x4 projectionMatrix = new Mat4x4();

    public static Vector3 getPos()             {return position;}
    public static float getZNear()             {return zNear;}
    public static float getZFar()              {return  zFar;}
    public static float getFov()               {return   fov;}
    public static float getAspectRatio()       {return aspectRatio;}

    public static Mat4x4 getLookAtInverseMatrix() {return lookAtInverse;}
    public static Mat4x4 getProjectionMatrix() {return projectionMatrix;}

    public static void setPos(Vector3 newPos)               {position    = newPos;}
    public static void setZNear(float newZNear)             {zNear       = newZNear;}
    public static void setZFar(float newZFar)               {zFar        = newZFar; }
    public static void setFov(float newFov)                 {fov         = newFov;  }
    public static void setAspectRatio(float newAspectRatio) {aspectRatio = newAspectRatio;}

    private static void updateLookAt(Vector3 target) {
        Vector3 newForward = Vector3.subtract(target, position);
        newForward = Vector3.getNormalized(newForward);

        Vector3 a = Vector3.multiply(newForward, Vector3.dot(up, newForward));
        Vector3 newUp = Vector3.subtract(up, a);
        newUp = Vector3.getNormalized(newUp);

        Vector3 newRight = Vector3.cross(newForward, newUp);

        Mat4x4 matrix = new Mat4x4();

        matrix.set(
            newRight.getX(), newRight.getY(), newRight.getZ(), 0f,
            newUp.getX(), newUp.getY(), newUp.getZ(), 0f,
            newForward.getX(), newForward.getY(), newForward.getZ(), 0f,
            position.getX(), position.getY(), position.getZ(), 1f
        );

        lookAt.set(matrix);
    }

    private static void updateLookAtInverse(Vector3 target) {
        Vector3 newForward = Vector3.subtract(target, position);
        newForward = Vector3.getNormalized(newForward);

        Vector3 a = Vector3.multiply(newForward, Vector3.dot(up, newForward));
        Vector3 newUp = Vector3.subtract(up, a);
        newUp = Vector3.getNormalized(newUp);

        Vector3 newRight = Vector3.cross(newForward, newUp);

        Mat4x4 matrix = new Mat4x4();

        matrix.set(
            newRight.getX(), newUp.getX(), newForward.getX(), 0f, // Row 1
            newRight.getY(), newUp.getY(), newForward.getY(), 0f, // Row 2
            newRight.getZ(), newUp.getZ(), newForward.getZ(), 0f, // Row 3
            
            -(position.getX() * newRight.getX() + position.getY() * newUp.getX() + position.getZ() * newForward.getX()), // Row4
            -(position.getX() * newRight.getY() + position.getY() * newUp.getY() + position.getZ() * newForward.getY()), // Row4
            -(position.getX() * newRight.getZ() + position.getY() * newUp.getZ() + position.getZ() * newForward.getZ()), // Row4
            1f
        );

        lookAtInverse.set(matrix);
    }

    public static void updateTransformMatrices() {
        Vector3 target = Vector3.add(position, forward);

        updateLookAt( target );
        updateLookAtInverse( target );
    }

    public static void updateProjectionMatrix() {
        float fovRad = (float) Math.toRadians(fov);
        
        projectionMatrix.set(0, 0, aspectRatio * fovRad);
        projectionMatrix.set(1, 1, fovRad);
        projectionMatrix.set(2, 2, zFar / (zFar - zNear));
        projectionMatrix.set(3, 2, (-zFar * zNear) / (zFar - zNear));
        projectionMatrix.set(2, 3, 1.0f);
        projectionMatrix.set(3, 3, 0f);
    }
}
