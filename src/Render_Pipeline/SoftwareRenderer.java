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

    //! Debug stuff should probably be removed or relocated l8r
    public boolean debugger_displayFPS = false;
    public int debugger_timeSinceLastFrameMS = 0;

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
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        // Absolutely disgusting stuff
        Mat4x4 projectionMatrix = Camera.getProjectionMatrix();
        for (Mesh mesh : meshes) {
            for (Triangle tri : mesh.getTriangles()) {
                Vector3[] vertices = tri.getVertices();
                Vector3 t0 = vertices[0];
                Vector3 t1 = vertices[1];
                Vector3 t2 = vertices[2];
            
                // Compute vectors and dot products once
                Vector3 cameraPosition = Camera.getPos();
                Vector3 t0ToCamera = Vector3.subtract(t0, cameraPosition);
                float normalTDotProduct = Vector3.dot(tri.getNormal(), t0ToCamera);
            
                if (normalTDotProduct > 0f) {
                    Vector3 projectedV0 = Vector3.applyProjectionMatrix(t0, projectionMatrix);
                    Vector3 projectedV1 = Vector3.applyProjectionMatrix(t1, projectionMatrix);
                    Vector3 projectedV2 = Vector3.applyProjectionMatrix(t2, projectionMatrix);
                
                    int x0 = (int) ((projectedV0.getX() + 1) * 0.5f * width);
                    int y0 = (int) ((projectedV0.getY() + 1) * 0.5f * height);
                    int x1 = (int) ((projectedV1.getX() + 1) * 0.5f * width);
                    int y1 = (int) ((projectedV1.getY() + 1) * 0.5f * height);
                    int x2 = (int) ((projectedV2.getX() + 1) * 0.5f * width);
                    int y2 = (int) ((projectedV2.getY() + 1) * 0.5f * height);
                
                    int[] boundingBox = calculateScreenBoundingBoxForTriangle(x0, y0, x1, y1, x2, y2);
                
                    // Calculate bounding box limits
                    int startY = Math.max(boundingBox[1], 0);
                    int endY = Math.min(boundingBox[5], height);
                    int startX = Math.max(boundingBox[0], 0);
                    int endX = Math.min(boundingBox[4], width);
                
                    // Rasterize the triangle
                    rasterizeTriangle(startX, endX, startY, endY, x0, y0, x1, y1, x2, y2);
                }
            }
        }
    }

    private void rasterizeTriangle(int startX, int endX, int startY, int endY, int x0, int y0, int x1, int y1, int x2, int y2) {
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if (isPointInsideTriangle(x, y, x0, y0, x1, y1, x2, y2)) {
                    setPixel(x, y, 0xFFFFFF);
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
                if (normalTDotProduct > 0f) {
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

                    drawTriangle(x0, y0, x1, y1, x2, y2, 0xFF0000);
                }
            }
        }
    }

    // Clock-Wise winding starting top-left, vertices must be projected
    private int[] calculateScreenBoundingBoxForTriangle(int v0x, int v0y, int v1x, int v1y, int v2x, int v2y) {
        int[] vertices = new int[8];

        int minX = Math.min(Math.min(v0x, v1x), v2x);
        int minY = Math.min(Math.min(v0y, v1y), v2y);
        int maxX = Math.max(Math.max(v0x, v1x), v2x);
        int maxY = Math.max(Math.max(v0y, v1y), v2y);

        int width  = maxX - minX;
        int height = maxY - minY;

        vertices[0] = minX;          // Top-Left X
        vertices[1] = minY;          // Top-Left Y
        vertices[2] = minX + width;  // Top-Right X
        vertices[3] = minY;          // Top-Right Y
        vertices[4] = minX + width;  // Bottom-Right X
        vertices[5] = minY + height; // Bottom-Right Y
        vertices[6] = minX;          // Bottom-Left X
        vertices[7] = minY + height; // Bottom-Left Y

        return vertices;
    }

    private boolean isPointInsideTriangle(int px, int py, int x0, int y0, int x1, int y1, int x2, int y2) {
        float areaOrig = Math.abs((x0 * (y1 - y2) + x1 * (y2 - y0) + x2 * (y0 - y1)) / 2.0f);
        float area1 = Math.abs((px * (y1 - y2) + x1 * (y2 - py) + x2 * (py - y1)) / 2.0f);
        float area2 = Math.abs((x0 * (py - y2) + px * (y2 - y0) + x2 * (y0 - py)) / 2.0f);
        float area3 = Math.abs((x0 * (y1 - py) + x1 * (py - y0) + px * (y0 - y1)) / 2.0f);
    
        return Math.abs(area1 + area2 + area3 - areaOrig) < 1e-6;
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
        g.drawImage(canvas, 0, 0, width, height, null);

        if (debugger_displayFPS) {
            Font baseFont = new Font("Arial", Font.PLAIN, 12);
            Font scaledFont = baseFont.deriveFont(64f);
            g.setFont(scaledFont);

            g.setColor(Color.WHITE);
            g.drawString(Integer.toString(debugger_timeSinceLastFrameMS) + "ms", 0 + (int) (width * 0.01f), height);
        }
    }
}