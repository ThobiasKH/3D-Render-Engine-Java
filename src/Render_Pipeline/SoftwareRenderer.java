package Render_Pipeline;

import Three_D_Components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SoftwareRenderer extends JFrame {

    private float scaleFactor = 1f;

    private BufferedImage canvas;
    private int width, height;

    ArrayList<Mesh> meshes = new ArrayList<Mesh>();

    public SoftwareRenderer(int width, int height) {
        this.width = width;
        this.height = height;

        setUndecorated(true); 
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 

        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setVisible(true);
    }

    public SoftwareRenderer(int width, int height, float scaleFactor) {
        this.width = width;
        this.height = height;
        this.scaleFactor = Math.clamp(scaleFactor, 0, 1);

        setUndecorated(true); 
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 

        canvas = new BufferedImage((int)(width * scaleFactor), (int)(height * scaleFactor), BufferedImage.TYPE_INT_RGB);

        setVisible(true);
    }

    public void setPixel(int x, int y, int color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            canvas.setRGB((int) (x * scaleFactor), (int) (y * scaleFactor), color);
        }
    }

    public void addMesh(Mesh mesh) {
        meshes.add(mesh);
    }

    public void renderMeshes() {
        Graphics2D g2d = canvas.createGraphics();
        g2d.scale(10, 2);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        for (Mesh mesh : meshes) {
            for (Triangle tri : mesh.getTriangles()) {

                Vector3[] vertices = tri.getVertices();

                Vector3 v0 = vertices[0];
                Vector3 v1 = vertices[1];
                Vector3 v2 = vertices[2];

                Mat4x4 projectionMatrix = Camera.getProjectionMatrix();

                Vector3 projectedV0 = Vector3.applyProjectionMatrix(v0, projectionMatrix);
                Vector3 projectedV1 = Vector3.applyProjectionMatrix(v1, projectionMatrix);
                Vector3 projectedV2 = Vector3.applyProjectionMatrix(v2, projectionMatrix);

                int x0 = (int) ( (projectedV0.getX() + 1) * 0.5f * (float)  width );
                int y0 = (int) ( (projectedV0.getY() + 1) * 0.5f * (float) height );
                int x1 = (int) ( (projectedV1.getX() + 1) * 0.5f * (float)  width );
                int y1 = (int) ( (projectedV1.getY() + 1) * 0.5f * (float) height );
                int x2 = (int) ( (projectedV2.getX() + 1) * 0.5f * (float)  width );
                int y2 = (int) ( (projectedV2.getY() + 1) * 0.5f * (float) height );

                drawTriangle(x0, y0, x1, y1, x2, y2, 0xFFFFFF);
            }
        }
    }

    private void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color) {
        drawLine(x0, y0, x1, y1, color);
        drawLine(x1, y1, x2, y2, color);
        drawLine(x2, y2, x0, y0, color);
    }

    private void drawLine(int x0, int y0, int x1, int y1, int color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;  // Step direction for x
        int sy = y0 < y1 ? 1 : -1;  // Step direction for y
        int err = dx - dy;

        while (true) {
            setPixel(x0, y0, color);  // Set the current pixel

            if (x0 == x1 && y0 == y1) break;  // End the loop if the line has been drawn

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    @Override
    public void paint(Graphics g) 
    {
        // super.paint(g);
        
        g.drawImage(canvas, 0, 0, width, height, null);
    }
}
