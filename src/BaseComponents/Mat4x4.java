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
}
