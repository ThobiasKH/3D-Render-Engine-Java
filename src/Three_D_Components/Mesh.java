package Three_D_Components;

public class Mesh {
    private Triangle[] triangles;
    public int numTriangles;

    public Mesh(Triangle[] triangles) {
        this.triangles = triangles;
        numTriangles = triangles.length;
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
}