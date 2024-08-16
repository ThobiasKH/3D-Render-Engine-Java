package Three_D_Components;

public class Mesh {
    private Triangle[] triangles;
    // private float[] rawVertices;
    public int numTriangles;

    public Mesh(Triangle[] triangles) {
        this.triangles = triangles;
        numTriangles = triangles.length;
        // packVertexData();
    }

    // public void packVertexData() {
        // rawVertices = new float[triangles.length * 3 * 3]; // NumTriangles * NumVerticiesInEachTriangle * NumComponentsInEachVertex
        // 
        // for (int i = 0; i < triangles.length; i++) {
            // Triangle currentTri = triangles[i];
            // Vector3[] currentVertices = currentTri.getVertices();
    // 
            // int baseIndex = i * 9; 
    // 
            // this.rawVertices[baseIndex]     = currentVertices[0].getX();
            // this.rawVertices[baseIndex + 1] = currentVertices[0].getY();
            // this.rawVertices[baseIndex + 2] = currentVertices[0].getZ();
            // this.rawVertices[baseIndex + 3] = currentVertices[1].getX();
            // this.rawVertices[baseIndex + 4] = currentVertices[1].getY();
            // this.rawVertices[baseIndex + 5] = currentVertices[1].getZ();
            // this.rawVertices[baseIndex + 6] = currentVertices[2].getX();
            // this.rawVertices[baseIndex + 7] = currentVertices[2].getY();
            // this.rawVertices[baseIndex + 8] = currentVertices[2].getZ();
        // }
    // }

    public Triangle[] getTriangles() { return triangles; }

    // public float[] getRawVertices() {return this.rawVertices;}
}