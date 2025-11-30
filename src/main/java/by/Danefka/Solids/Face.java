package by.Danefka.Solids;

public class Face {
    int numVerts;
    double[] norm;
    double w;
    int[] verts;

    public Face(int numVerts, double[] norm, double w, int[] verts) {
        this.numVerts = numVerts;
        this.norm = norm;
        this.w = w;
        this.verts = verts;
    }


    public int getNumVerts() {
        return numVerts;
    }

    public double[] getNorm() {
        return norm;
    }

    public double getW() {
        return w;
    }

    public int[] getVerts() {
        return verts;
    }
}
