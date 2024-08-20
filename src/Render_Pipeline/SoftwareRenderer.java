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
    private int canvasWidth, canvasHeight;
    private int canvasWidthInverse, canvasHeightInverse;

    ArrayList<Mesh> meshes = new ArrayList<Mesh>();

    public SoftwareRenderer(int width, int height) {
        this.width = width;
        this.height = height;

        canvasWidth = (int) (width * scaleFactor);
        canvasHeight = (int) (height * scaleFactor);
        canvasWidthInverse = (int) (width / scaleFactor);
        canvasHeightInverse = (int) (height / scaleFactor);

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

        canvasWidth = (int) (width * scaleFactor);
        canvasHeight = (int) (height * scaleFactor);
        canvasWidthInverse = (int) (width / scaleFactor);
        canvasHeightInverse = (int) (height / scaleFactor);

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
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        // Absolutely disgusting stuff
        Mat4x4 projectionMatrix = Camera.getProjectionMatrix();
        Vector3 cameraPosition = Camera.getPos();
        for (Mesh mesh : meshes) {
            for (Triangle tri : mesh.getTriangles()) {
                Vector3[] vertices = tri.getVertices();
                Vector3 t0 = vertices[0];
                Vector3 t1 = vertices[1];
                Vector3 t2 = vertices[2];
            
                Vector3 t0ToCamera = Vector3.subtract(t0, cameraPosition);
                float normalTDotProduct = Vector3.dot(tri.getNormal(), t0ToCamera);
            
                if (normalTDotProduct < 0) {
                    Vector3 projectedV0 = Vector3.applyProjectionMatrix(t0, projectionMatrix);
                    Vector3 projectedV1 = Vector3.applyProjectionMatrix(t1, projectionMatrix);
                    Vector3 projectedV2 = Vector3.applyProjectionMatrix(t2, projectionMatrix);
                
                    int x0 = (int) ( (projectedV0.getX() + 1) * 0.5f * (float)  canvasWidth);
                    int y0 = (int) ( (projectedV0.getY() + 1) * 0.5f * (float) canvasHeight);
                    int x1 = (int) ( (projectedV1.getX() + 1) * 0.5f * (float)  canvasWidth);
                    int y1 = (int) ( (projectedV1.getY() + 1) * 0.5f * (float) canvasHeight);
                    int x2 = (int) ( (projectedV2.getX() + 1) * 0.5f * (float)  canvasWidth);
                    int y2 = (int) ( (projectedV2.getY() + 1) * 0.5f * (float) canvasHeight);
                
                    int[] boundingBox = calculateScreenBoundingBoxForTriangle(x0, y0, x1, y1, x2, y2);
                
                    int startY = Math.max(boundingBox[1], 0);
                    int endY = Math.min(boundingBox[3], canvasHeight);
                    int startX = Math.max(boundingBox[0], 0);
                    int endX = Math.min(boundingBox[2], canvasWidth);
                
                    rasterizeTriangle(tri.DEBUGCOLOR, startX, endX, startY, endY, x0, y0, x1, y1, x2, y2);
                }
            }
        }
    }

    private void rasterizeTriangle(int color, int startX, int endX, int startY, int endY, int x0, int y0, int x1, int y1, int x2, int y2) {
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if (isPointInsideTriangle(x, y, x0, y0, x1, y1, x2, y2)) {
                    setPixel(x, y, color);
                }
            }
        }
    }

    public void renderMeshesWireframe() {
        // scaleFactor += 0.0001f;
        // canvas = new BufferedImage((int)(width * scaleFactor), (int)(height * scaleFactor), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        for (Mesh mesh : meshes) {
            for (Triangle tri : mesh.getTriangles()) {

                Vector3[] vertices = tri.getVertices();

                Vector3 v0 = vertices[0];
                Vector3 v1 = vertices[1];
                Vector3 v2 = vertices[2];

                Vector3 normal = tri.getNormal();

                Vector3 cameraPosition = Camera.getPos();
                Vector3 t = new Vector3(v0.getX() - cameraPosition.getX(), v0.getY() - cameraPosition.getY(), v0.getZ() - cameraPosition.getZ());
                float normalTDotProduct = Vector3.dot(normal, t);

                // if (normal.getZ() > 0) {
                if (normalTDotProduct < 0f) {
                    Mat4x4 projectionMatrix = Camera.getProjectionMatrix();

                    Vector3 projectedV0 = Vector3.applyProjectionMatrix(v0, projectionMatrix);
                    Vector3 projectedV1 = Vector3.applyProjectionMatrix(v1, projectionMatrix);
                    Vector3 projectedV2 = Vector3.applyProjectionMatrix(v2, projectionMatrix);

                    int x0 = (int) ( (projectedV0.getX() + 1) * 0.5f * (float)  canvasWidth);
                    int y0 = (int) ( (projectedV0.getY() + 1) * 0.5f * (float) canvasHeight);
                    int x1 = (int) ( (projectedV1.getX() + 1) * 0.5f * (float)  canvasWidth);
                    int y1 = (int) ( (projectedV1.getY() + 1) * 0.5f * (float) canvasHeight);
                    int x2 = (int) ( (projectedV2.getX() + 1) * 0.5f * (float)  canvasWidth);
                    int y2 = (int) ( (projectedV2.getY() + 1) * 0.5f * (float) canvasHeight);

                    drawTriangle(x0, y0, x1, y1, x2, y2, 0xFF0000);
                }
            }
        }
    }

    // Clock-Wise winding starting top-left, vertices must be projected
    private int[] calculateScreenBoundingBoxForTriangle(int x0, int y0, int x1, int y1, int x2, int y2) {
        int minX = Math.min(x0, Math.min(x1, x2));
        int maxX = Math.max(x0, Math.max(x1, x2));
        int minY = Math.min(y0, Math.min(y1, y2));
        int maxY = Math.max(y0, Math.max(y1, y2));
    
        return new int[]{minX, minY, maxX, maxY};
    }

    public boolean isPointInsideTriangle(int px, int py, int x1, int y1, int x2, int y2, int x3, int y3) {
        float denominator = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);
        float lambda1 = ((y2 - y3) * (px - x3) + (x3 - x2) * (py - y3)) / denominator;
        float lambda2 = ((y3 - y1) * (px - x3) + (x1 - x3) * (py - y3)) / denominator;
        float lambda3 = 1.0f - lambda1 - lambda2;
    
        return lambda1 >= 0 && lambda2 >= 0 && lambda3 >= 0;
    }

    // This method should be faster, but isn't for some reason
    // public boolean isPointInsideTriangle(int px, int py, int x1, int y1, int x2, int y2, int x3, int y3) {
    //     int d1 = sign(px, py, x1, y1, x2, y2);
    //     int d2 = sign(px, py, x2, y2, x3, y3);
    //     int d3 = sign(px, py, x3, y3, x1, y1);
    
    //     boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
    //     boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);
    
    //     return !(hasNeg && hasPos);
    // }
    
    // private int sign(int px, int py, int x1, int y1, int x2, int y2) {
    //     return (px - x2) * (y1 - y2) - (x1 - x2) * (py - y2);
    // }

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
        g.drawImage(canvas, 0, 0, canvasWidthInverse, canvasHeightInverse, null);
    }
}