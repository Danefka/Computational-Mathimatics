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
}
