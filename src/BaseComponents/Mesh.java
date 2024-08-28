package BaseComponents;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private Triangle[] triangles;
    public int numTriangles;

    public Mesh(Triangle[] triangles) {
        this.triangles = triangles;
        numTriangles = triangles.length;
    }

    public Mesh(String filePath) {
        try {
            loadFromObjectFile(filePath);
        } catch (IOException ex) {
        }
    }

    public Triangle[] getTriangles() { return this.triangles; }

    public Vector3 getCenter() {
        int numVertices = this.triangles.length * 3;

        float centerX = 0f;
        float centerY = 0f;
        float centerZ = 0f;

        for (Triangle tri : this.triangles) {
            for (Vector3 v : tri.getVertices()) {
                centerX += v.getX();
                centerY += v.getY();
                centerZ += v.getZ();
            }
        }

        centerX /= numVertices;
        centerY /= numVertices;
        centerZ /= numVertices;

        return new Vector3(centerX, centerY, centerZ);
    }

    private void loadFromObjectFile(String filePath) throws IOException {
        List<Vector3> vertices = new ArrayList<>();
        List<Triangle> tris = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
    
                String[] tokens = line.split("\\s+");
                if (tokens.length == 0) continue;
    
                switch (tokens[0]) {
                    case "v":
                        if (tokens.length >= 4) {
                            try {
                                float x = Float.parseFloat(tokens[1]);
                                float y = Float.parseFloat(tokens[2]);
                                float z = Float.parseFloat(tokens[3]);
                                vertices.add(new Vector3(x, y, z));
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid vertex format: " + line);
                            }
                        } else {
                            System.err.println("Incomplete vertex data: " + line);
                        }
                        break;
    
                    case "f":
                        if (tokens.length >= 4) {
                            try {
                                // Parse vertex indices (only consider the first part before the '/' if there's more)
                                int v1Index = Integer.parseInt(tokens[1].split("/")[0]) - 1;
                                int v2Index = Integer.parseInt(tokens[2].split("/")[0]) - 1;
                                int v3Index = Integer.parseInt(tokens[3].split("/")[0]) - 1;
    
                                if (tokens.length == 4) {
                                    // Triangle face
                                    if (v1Index >= 0 && v1Index < vertices.size() &&
                                        v2Index >= 0 && v2Index < vertices.size() &&
                                        v3Index >= 0 && v3Index < vertices.size()) {
    
                                        Vector3 v1 = vertices.get(v1Index);
                                        Vector3 v2 = vertices.get(v2Index);
                                        Vector3 v3 = vertices.get(v3Index);
    
                                        tris.add(new Triangle(v1, v2, v3));
                                    } else {
                                        System.err.println("Face references out-of-bounds vertices: " + line);
                                    }
                                } else if (tokens.length == 5) {
                                    // Quadrilateral face, split into two triangles
                                    int v4Index = Integer.parseInt(tokens[4].split("/")[0]) - 1;
    
                                    if (v1Index >= 0 && v1Index < vertices.size() &&
                                        v2Index >= 0 && v2Index < vertices.size() &&
                                        v3Index >= 0 && v3Index < vertices.size() &&
                                        v4Index >= 0 && v4Index < vertices.size()) {
    
                                        Vector3 v1 = vertices.get(v1Index);
                                        Vector3 v2 = vertices.get(v2Index);
                                        Vector3 v3 = vertices.get(v3Index);
                                        Vector3 v4 = vertices.get(v4Index);
    
                                        // First triangle: v1, v2, v3
                                        tris.add(new Triangle(v1, v2, v3));
                                        // Second triangle: v1, v3, v4
                                        tris.add(new Triangle(v1, v3, v4));
                                    } else {
                                        System.err.println("Face references out-of-bounds vertices: " + line);
                                    }
                                } else {
                                    System.err.println("Unsupported face format (more than 4 vertices): " + line);
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid face format: " + line);
                            }
                        } else {
                            System.err.println("Invalid face data (not a triangle): " + line);
                        }
                        break;
    
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error reading OBJ file: " + filePath, e);
        }
    
        // Safely assign the triangles array
        if (tris.isEmpty()) {
            System.err.println("No triangles found in the OBJ file.");
            this.triangles = new Triangle[0]; // Initialize as an empty array
        } else {
            this.triangles = tris.toArray(new Triangle[0]);
        }
    
        this.numTriangles = this.triangles.length;
    }

    public void move(Vector3 movementVector) {
        for (Triangle tri : this.triangles) {
            tri.move(movementVector);
        }
    }

    public void moveTo(Vector3 newPosition) {
        Vector3 currentCenter = this.getCenter();

        Vector3 translation = Vector3.subtract(newPosition, currentCenter);

        this.move(translation);
    }

    public void rotateAroundXAxisWithPoint(float angleInRadians, Vector3 point) {
        for (Triangle tri : this.triangles) {
            tri.rotateAroundXAxisWithPoint(angleInRadians, point);
        }
    }

    public void rotateAroundYAxisWithPoint(float angleInRadians, Vector3 point) {
        for (Triangle tri : this.triangles) {
            tri.rotateAroundYAxisWithPoint(angleInRadians, point);
        }
    }

    public void rotateAroundZAxisWithPoint(float angleInRadians, Vector3 point) {
        for (Triangle tri : this.triangles) {
            tri.rotateAroundZAxisWithPoint(angleInRadians, point);
        }
    }

    public static Mesh createMeshCube(float dimension, Vector3 position) {
        float halfDimension = dimension / 2;
        float posX = position.getX();
        float posY = position.getY();
        float posZ = position.getZ();

        // Front
        Triangle front1 = new Triangle(
            posX - halfDimension, posY - halfDimension, posZ - halfDimension,
            posX + halfDimension, posY + halfDimension, posZ - halfDimension,
            posX + halfDimension, posY - halfDimension, posZ - halfDimension
        );

        Triangle front2 = new Triangle(
            posX - halfDimension, posY - halfDimension, posZ - halfDimension,
            posX - halfDimension, posY + halfDimension, posZ - halfDimension,
            posX + halfDimension, posY + halfDimension, posZ - halfDimension
        );

        // Back
        Triangle back1 = new Triangle(
            posX - halfDimension, posY - halfDimension, posZ + halfDimension,
            posX + halfDimension, posY - halfDimension, posZ + halfDimension,
            posX + halfDimension, posY + halfDimension, posZ + halfDimension
        );

        Triangle back2 = new Triangle(
            posX - halfDimension, posY - halfDimension, posZ + halfDimension,
            posX + halfDimension, posY + halfDimension, posZ + halfDimension,
            posX - halfDimension, posY + halfDimension, posZ + halfDimension
        );

        // Left
        Triangle left1 = new Triangle(
            posX - halfDimension, posY - halfDimension, posZ + halfDimension,
            posX - halfDimension, posY + halfDimension, posZ - halfDimension,
            posX - halfDimension, posY - halfDimension, posZ - halfDimension
        );

        Triangle left2 = new Triangle(
            posX - halfDimension, posY - halfDimension, posZ + halfDimension,
            posX - halfDimension, posY + halfDimension, posZ + halfDimension,
            posX - halfDimension, posY + halfDimension, posZ - halfDimension
        );

        // Right
        Triangle right1 = new Triangle(
            posX + halfDimension, posY - halfDimension, posZ + halfDimension,
            posX + halfDimension, posY - halfDimension, posZ - halfDimension,
            posX + halfDimension, posY + halfDimension, posZ + halfDimension
        );

        Triangle right2 = new Triangle(
            posX + halfDimension, posY - halfDimension, posZ - halfDimension,
            posX + halfDimension, posY + halfDimension, posZ - halfDimension,
            posX + halfDimension, posY + halfDimension, posZ + halfDimension
        );

        // Top
        Triangle top1 = new Triangle(
            posX - halfDimension, posY + halfDimension, posZ - halfDimension,
            posX + halfDimension, posY + halfDimension, posZ + halfDimension,
            posX + halfDimension, posY + halfDimension, posZ - halfDimension
        );

        Triangle top2 = new Triangle(
            posX - halfDimension, posY + halfDimension, posZ - halfDimension,
            posX - halfDimension, posY + halfDimension, posZ + halfDimension,
            posX + halfDimension, posY + halfDimension, posZ + halfDimension
        );

        // Bottom
        Triangle bot1 = new Triangle(
            posX - halfDimension, posY - halfDimension, posZ - halfDimension,
            posX + halfDimension, posY - halfDimension, posZ - halfDimension,
            posX + halfDimension, posY - halfDimension, posZ + halfDimension
        );

        Triangle bot2 = new Triangle(
            posX - halfDimension, posY - halfDimension, posZ - halfDimension,
            posX + halfDimension, posY - halfDimension, posZ + halfDimension,
            posX - halfDimension, posY - halfDimension, posZ + halfDimension
        );

        Triangle[] tris = {front1, front2, back1, back2, left1, left2, right1, right2, top1, top2, bot1, bot2}; 

        Mesh meshCube = new Mesh(tris);
        return meshCube;
    }
}