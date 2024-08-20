package Render_Pipeline;
import Three_D_Components.*;

public class Camera {
    private static Vector3 position = new Vector3(0, 0, 0);

    private static float zNear = 1f;
    private static float zFar = 1000f;
    private static float fov = 90f; // In degrees 
    private static float aspectRatio = .5f;
    private static Mat4x4 projectionMatrix = new Mat4x4();

    public static Vector3 getPos()             {return position;}
    public static float getZNear()             {return zNear;}
    public static float getZFar()              {return  zFar;}
    public static float getFov()               {return   fov;}
    public static float getAspectRatio()       {return aspectRatio;}

    public static Mat4x4 getProjectionMatrix() {return projectionMatrix;}

    public static void setPos(Vector3 newPos)               {position    = newPos;}
    public static void setZNear(float newZNear)             {zNear       = newZNear;}
    public static void setZFar(float newZFar)               {zFar        = newZFar; }
    public static void setFov(float newFov)                 {fov         = newFov;  }
    public static void setAspectRatio(float newAspectRatio) {aspectRatio = newAspectRatio;}

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
