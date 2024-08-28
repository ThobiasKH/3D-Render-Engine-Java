import BaseComponents.*;
import RenderPipeline.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

public class App {
    static final int FPS_TARGET = 60;
    public static void main(String[] args) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        float resScaleFactor = 1f;

        Camera.setAspectRatio( (float) screenHeight / (float) screenWidth );
        Camera.updateProjectionMatrix();
        Camera.updateTransformMatrices();

        SoftwareRenderer renderer = new SoftwareRenderer(screenWidth, screenHeight, resScaleFactor);

        // Mesh meshCube = Mesh.createMeshCube(2, new Vector3(0, 0, 5));
        // Mesh meshCube = new Mesh("src/models/12140_Skull_v3_L2.obj");
        // Mesh meshCube = new Mesh("src/models/Sting-Sword-lowpoly.obj");
        Mesh meshCube = new Mesh("src/models/lego obj.obj");
        meshCube.move(new Vector3(-45, -20, 85));
        meshCube.rotateAroundXAxisWithPoint((float)Math.PI, meshCube.getCenter());
        renderer.addMesh(meshCube);

        DirectionalLight light1 = new DirectionalLight(1f, new Vector3(1, 0, -0));
        renderer.setDirectionalLight(light1);


        Random rand = new Random();
        int randomColor = 0;
        Triangle[] cubeTris = meshCube.getTriangles();
        for (int i = 0; i < 12; i++) {
            if (i % 2 == 0) randomColor = rand.nextInt();
            cubeTris[i].setColor(randomColor);
        } 

        long startTime = System.currentTimeMillis();
        long deltaTime = 1;

        renderer.DEBUG_DRAW_WIREFRAME = true;

        while (true) {
            Vector3 cubeCenter = meshCube.getCenter();

            // meshCube.rotateAroundXAxisWithPoint(0.001f * deltaTime, cubeCenter);
            // meshCube.rotateAroundYAxisWithPoint(0.001f * deltaTime, cubeCenter);
            // meshCube.rotateAroundZAxisWithPoint(0.001f * deltaTime, cubeCenter);

            if (SoftwareRenderer.mouseIsDown) {
                meshCube.rotateAroundYAxisWithPoint(0.0001f * deltaTime * -SoftwareRenderer.mouseDiffX, cubeCenter);
                meshCube.rotateAroundXAxisWithPoint(0.0001f * deltaTime * SoftwareRenderer.mouseDiffY, cubeCenter);
                // light1.rotateAroundYAxis(0.0001f * deltaTime * -SoftwareRenderer.mouseDiffX);
                // light1.rotateAroundXAxis(0.0001f * deltaTime * SoftwareRenderer.mouseDiffY);
            }

            renderer.renderMeshes();

            renderer.repaint();
        
            long endTime = System.currentTimeMillis();
            deltaTime = endTime - startTime;
            startTime = endTime;

            // System.out.println("Render: " + deltaTime + "ms");

            renderer.lockMouse();

            SoftwareRenderer.mouseDiffX = 0;
            SoftwareRenderer.mouseDiffY = 0;

            try {
                Thread.sleep(1000 / FPS_TARGET);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
