package Three_D_Components;

public class Triangle {
    private Vector3[] p;

    public Triangle(Vector3[] vectors) {
        if (vectors.length != 3) {
            throw new IllegalArgumentException("Error in Triangle construction, vector array must be of length 3");
        }
        this.p = vectors.clone();
    }

    public Triangle(Vector3 v0, Vector3 v1, Vector3 v2) {
        this.p[0] = v0;
        this.p[1] = v1;
        this.p[2] = v2;
    }

    public Triangle(float p1, float p2, float p3, float p4, float p5, float p6, float p7, float p8, float p9) {
        Vector3 v1 = new Vector3(p1, p2, p3);
        Vector3 v2 = new Vector3(p4, p5, p6);
        Vector3 v3 = new Vector3(p7, p8, p9);

        this.p = new Vector3[3];
        this.p[0] = v1;
        this.p[1] = v2;
        this.p[2] = v3;
    }

    public Vector3[] getVertices() { return p; }
}
