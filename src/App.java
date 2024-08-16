import Three_D_Components.*;
import Three_D_Components.Vector3;

import java.awt.Dimension;
import java.awt.Toolkit;

import Render_Pipeline.*;

public class App {
    public static void main(String[] args) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        float resScaleFactor = 0.5f;

        Camera.setAspectRatio( (float) screenHeight / (float) screenWidth );
        // Camera.setAspectRatio( (float) screenWidth / (float) screenHeight );
        Camera.updateProjectionMatrix();

        SoftwareRenderer renderer = new SoftwareRenderer(screenWidth, screenHeight, resScaleFactor);
        
        float xMod = 0f;

        Triangle tri1 = new Triangle(
            -0.5f - xMod, -0.5f, -0.5f + 2,  // v0
            0.5f - xMod, -0.5f, -0.5f + 2,   // v1
            0.5f - xMod,  0.5f, -0.5f + 2    // v2
        );

        Triangle tri2 = new Triangle(
            -0.5f - xMod, -0.5f, -0.5f + 2,  // v0
            0.5f - xMod,  0.5f, -0.5f + 2,   // v2
            -0.5f - xMod,  0.5f, -0.5f  + 2  // v3
        );

        // Back face
        Triangle tri3 = new Triangle(
            0.5f - xMod, -0.5f,  0.5f + 2,   // v5
            -0.5f - xMod, -0.5f,  0.5f + 2,  // v4
            -0.5f - xMod,  0.5f,  0.5f + 2   // v7
        );

        Triangle tri4 = new Triangle(
            0.5f - xMod, -0.5f,  0.5f + 2,   // v5
            -0.5f - xMod,  0.5f,  0.5f + 2,  // v7
            0.5f - xMod,  0.5f,  0.5f + 2    // v6
        );

        // Left face
        Triangle tri5 = new Triangle(
            -0.5f - xMod, -0.5f,  0.5f + 2,  // v4
            -0.5f - xMod, -0.5f, -0.5f + 2,  // v0
            -0.5f - xMod,  0.5f, -0.5f + 2   // v3
        );

        Triangle tri6 = new Triangle(
            -0.5f - xMod, -0.5f,  0.5f + 2,  // v4
            -0.5f - xMod,  0.5f, -0.5f + 2,  // v3
            -0.5f - xMod,  0.5f,  0.5f + 2   // v7
        );

        // Right face
        Triangle tri7 = new Triangle(
            0.5f - xMod, -0.5f, -0.5f + 2,   // v1
            0.5f - xMod, -0.5f,  0.5f + 2,   // v5
            0.5f - xMod,  0.5f,  0.5f + 2    // v6
        );

        Triangle tri8 = new Triangle(
            0.5f - xMod, -0.5f, -0.5f + 2,   // v1
            0.5f - xMod,  0.5f,  0.5f + 2,   // v6
            0.5f - xMod,  0.5f, -0.5f + 2    // v2
        );

        // Top face
        Triangle tri9 = new Triangle(
            -0.5f - xMod,  0.5f, -0.5f + 2,  // v3
            0.5f - xMod,  0.5f, -0.5f + 2,   // v2
            0.5f - xMod,  0.5f,  0.5f + 2    // v6
        );

        Triangle tri10 = new Triangle(
            -0.5f - xMod,  0.5f, -0.5f + 2,  // v3
            0.5f - xMod,  0.5f,  0.5f + 2,   // v6
            -0.5f - xMod,  0.5f,  0.5f + 2   // v7
        );

        // Bottom face
        Triangle tri11 = new Triangle(
            -0.5f - xMod, -0.5f,  0.5f + 2,  // v4
            0.5f - xMod, -0.5f,  0.5f + 2,   // v5
            0.5f - xMod, -0.5f, -0.5f + 2    // v1
        );

        Triangle tri12 = new Triangle(
            -0.5f - xMod, -0.5f,  0.5f + 2,  // v4
            0.5f - xMod, -0.5f, -0.5f + 2,   // v1
            -0.5f - xMod, -0.5f, -0.5f + 2   // v0
        );

        Triangle[] cubeTriangles = {tri1, tri2, tri3, tri4, tri5, tri6, tri7, tri8, tri9, tri10, tri11, tri12};

        Mesh meshCube = new Mesh(cubeTriangles);
        renderer.addMesh(meshCube);

        while (true) {
            Vector3 cubeCenter = meshCube.getCenter();
            meshCube.rotateAroundXAxisWithPoint(0.01f, cubeCenter);
            meshCube.rotateAroundYAxisWithPoint(0.01f, cubeCenter);
            meshCube.rotateAroundZAxisWithPoint(0.01f, cubeCenter);

            renderer.renderMeshes();

            renderer.repaint();

            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}