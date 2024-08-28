package BaseComponents;

public class Mat4x4 {
    private float[][] m;

    public Mat4x4() {
        this.m = new float[4][4];
    }

    public float[][] get() {
        return this.m;
    }

    public float get(int row, int col) {
        return this.m[row][col];
    }

    public void set(int row, int col, float value) {
        this.m[row][col] = value;
    }

    public void set(
            float x0, float y0, float z0, float w0,
            float x1, float y1, float z1, float w1,
            float x2, float y2, float z2, float w2,
            float x3, float y3, float z3, float w3
        ) {

        this.m[0][0] = x0;
        this.m[0][1] = y0;
        this.m[0][2] = z0;
        this.m[0][3] = w0;
        
        this.m[1][0] = x1;
        this.m[1][1] = y1;
        this.m[1][2] = z1;
        this.m[1][3] = w1;

        this.m[2][0] = x2;
        this.m[2][1] = y2;
        this.m[2][2] = z2;
        this.m[2][3] = w2;

        this.m[3][0] = x3;
        this.m[3][1] = y3;
        this.m[3][2] = z3;
        this.m[3][3] = w3;
    }

    public void set(Mat4x4 matrix) {
        this.m = matrix.get();
    }
}
