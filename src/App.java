import Three_D_Components.*;
import Three_D_Components.Vector3;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import Render_Pipeline.*;

public class App {
    static int desiredFPS = 60;
    public static void main(String[] args) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        float resScaleFactor = .25f;

        Camera.setAspectRatio( (float) screenHeight / (float) screenWidth );
        Camera.updateProjectionMatrix();

        SoftwareRenderer renderer = new SoftwareRenderer(screenWidth, screenHeight, resScaleFactor);

        Mesh meshCube = Mesh.createMeshCube(2, new Vector3(0, 0, 5));

        renderer.addMesh(meshCube);

        Random rand = new Random();
        int randomColor = 0;
        Triangle[] cubeTris = meshCube.getTriangles();
        for (int i = 0; i < 12; i++) {
            if (i % 2 == 0) randomColor = rand.nextInt();
            cubeTris[i].DEBUGCOLOR = randomColor;
        } 

        while (true) {
            Vector3 cubeCenter = meshCube.getCenter();
            // meshCube.move(new Vector3(0, 0, 0.01f));

            // meshCube.rotateAroundXAxisWithPoint(0.01f, cubeCenter);
            meshCube.rotateAroundYAxisWithPoint(0.01f, cubeCenter);
            // meshCube.rotateAroundZAxisWithPoint(0.01f, cubeCenter);

            long startTime = System.currentTimeMillis();

            renderer.renderMeshes();

            System.out.println("Render: " + (System.currentTimeMillis() - startTime) + "ms");

            renderer.repaint();

            try {
                Thread.sleep(1000 / desiredFPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}