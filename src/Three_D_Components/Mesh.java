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