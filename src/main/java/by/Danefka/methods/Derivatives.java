package by.Danefka.methods;

import by.Danefka.Utils.MatrixUtils;
import by.Danefka.Utils.VectorUtils;

public class Derivatives {

    public double[] dCenterOfMass;
    public double[] dMomentum;
    public double[] dAngularMomentum;
    public double[][] dRotationMatrix;

    public Derivatives(double[] dCenterOfMass,
                       double[] dMomentum,
                       double[] dAngularMomentum,
                       double[][] dRotationMatrix){
        this.dCenterOfMass = dCenterOfMass;
        this.dMomentum = dMomentum;
        this.dAngularMomentum = dAngularMomentum;
        this.dRotationMatrix = dRotationMatrix;
    }

    public Derivatives add(Derivatives k) {
        return new Derivatives(
                add(dCenterOfMass, k.dCenterOfMass),
                add(dMomentum, k.dMomentum),
                add(dAngularMomentum, k.dAngularMomentum),
                add(dRotationMatrix, k.dRotationMatrix)
        );
    }

    public Derivatives scale(double s) {
        return new Derivatives(
                scale(dCenterOfMass, s),
                scale(dMomentum, s),
                scale(dAngularMomentum, s),
                scale(dRotationMatrix, s)
        );
    }

    private static double[] add(double[] a, double[] b) {
        return VectorUtils.vectorSum(a,b);
    }

    private static double[][] add(double[][] a, double[][] b) {
        return MatrixUtils.addMatrices(a,b);
    }

    private static double[] scale(double[] a, double s) {
        return VectorUtils.multiplyByScalar(a,s);
    }

    private static double[][] scale(double[][] a, double s) {
        return MatrixUtils.multiplyByScalar(a,s);
    }
}
