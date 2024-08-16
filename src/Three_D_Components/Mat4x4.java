package Three_D_Components;

public class Mat4x4 {
    private float[][] m;

    public Mat4x4() {
        this.m = new float[4][4];
    }

    public static Vector3 vec3Multiplication(Vector3 inputVector, Mat4x4 transformationMatrix) {
        float inputX = inputVector.getX();
        float inputY = inputVector.getY();
        float inputZ = inputVector.getZ();

        float resultX = inputX * transformationMatrix.get(0, 0) + inputY * transformationMatrix.get(1, 0) + inputZ * transformationMatrix.get(2, 0) + transformationMatrix.get(3, 0);
        float resultY = inputX * transformationMatrix.get(0, 1) + inputY * transformationMatrix.get(1, 1) + inputZ * transformationMatrix.get(2, 1) + transformationMatrix.get(3, 1);
        float resultZ = inputX * transformationMatrix.get(0, 2) + inputY * transformationMatrix.get(1, 2) + inputZ * transformationMatrix.get(2, 2) + transformationMatrix.get(3, 2);
        float w       = inputX * transformationMatrix.get(0, 3) + inputY * transformationMatrix.get(1, 3) + inputZ * transformationMatrix.get(2, 3) + transformationMatrix.get(3, 3);
        
        if (w != 0f) {
            resultX /= w;
            resultY /= w;
            resultZ /= w;
        }

        return new Vector3(resultX, resultY, resultZ);
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
