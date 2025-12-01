package by.Danefka.Utils;

public class VectorUtils {



    public static double[] vectorSum(double[] v1, double[] v2){
        return new double[]{v1[0] + v2[0], v1[1] + v2[1], v1[2] + v2[2]};
    }

    public static double[] vectorSub(double[] v1, double[] v2) {
        return new double[]{v1[0] - v2[0], v1[1] - v2[1], v1[2] - v2[2]};
    }

    public static double[] multiplyByScalar(double[] a, double s) {
        double[] r = new double[a.length];
        for (int i = 0; i < a.length; i++) r[i] = a[i] * s;
        return r;
    }

    public static double[] crossProduct(double[] f, double[] s) {
        return new double[]{
                f[1] * s[2] - f[2] * s[1],
                f[2] * s[0] - f[0] * s[2],
                f[0] * s[1] - f[1] * s[0]
        };
    }

    public static double dotProduct(double[] f, double[] s) {
        return f[0]*s[0] + f[1]*s[1] + f[2]+s[2];
    }
}
