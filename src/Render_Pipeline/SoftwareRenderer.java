package Render_Pipeline;

import javax.swing.*;

import BaseComponents.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SoftwareRenderer extends JFrame {

    private float scaleFactor = 1f;

    private BufferedImage canvas;
    private int width, height;
    private int canvasWidth, canvasHeight;
    private int canvasWidthInverse, canvasHeightInverse;

    public boolean DEBUG_DRAW_WIREFRAME = false;
    public int DEBUG_DRAW_WIREFRAME_COLOR = 0x000000;

    private ArrayList<Mesh> meshes = new ArrayList<Mesh>();
    private DirectionalLight directionalLight;

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

    public void setDirectionalLight(DirectionalLight light) {
        this.directionalLight = light;
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
            
                Vector3 triNormal = tri.getNormal();
                Vector3 t0ToCamera = Vector3.subtract(t0, cameraPosition);
                float normalTDotProduct = Vector3.dot(triNormal, t0ToCamera);
            
                if (normalTDotProduct < 0) {
                    int color = tri.getColor();
                    if (this.directionalLight != null) {
                        Vector3 lightDirection = this.directionalLight.getDirection();
                        float dp = Vector3.dot(triNormal, lightDirection);
                        color = adjustColorForLight(color, dp * this.directionalLight.getIntensity());
                    }

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
                
                    rasterizeTriangle(color, startX, endX, startY, endY, x0, y0, x1, y1, x2, y2);

                    if (DEBUG_DRAW_WIREFRAME) {
                        drawTriangle(x0, y0, x1, y1, x2, y2, DEBUG_DRAW_WIREFRAME_COLOR);
                    }
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
            setPixel(x0, y0, color);

            if (x0 == x1 && y0 == y1) break;

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

    public static int adjustColorForLight(int color, float strength) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        float normStrength = (strength + 1) * 0.5f;

        int adjustedRed = (int) (red * strength);
        int adjustedGreen = (int) (green * strength);
        int adjustedBlue = (int) (blue * strength);
    
        adjustedRed = Math.max(0, Math.min(255, adjustedRed));
        adjustedGreen = Math.max(0, Math.min(255, adjustedGreen));
        adjustedBlue = Math.max(0, Math.min(255, adjustedBlue));
    
        return (adjustedRed << 16) | (adjustedGreen << 8) | adjustedBlue;
    }

    @Override
    public void paint(Graphics g) 
    {
        g.drawImage(canvas, 0, 0, canvasWidthInverse, canvasHeightInverse, null);
    }
}