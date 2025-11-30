package by.Danefka;

import by.Danefka.Utils.MatrixUtils;
import by.Danefka.Utils.VectorUtils;
import by.Danefka.methods.Derivatives;

public class State {
    private double[] centerOfMass;
    private double[] momentum;
    private double[][] rotationMatrix;
    private double[] angularMomentum;

    private double[] velocity;
    private double[] force;
    private double[][] derivativeOfTheRotationMatrix;
    private double[] momentOfForce;

    public State() {
        centerOfMass = new double[3];
        momentum = new double[3];
        rotationMatrix = new double[3][3];
        angularMomentum = new double[3];

        velocity = new double[3];
        force = new double[3];
        derivativeOfTheRotationMatrix = new double[3][3];
        momentOfForce = new double[3];
    }

    public State(double[] centerOfMass, double[] momentum, double[][] rotationMatrix, double[] angularMomentum, double[] velocity, double[] force, double[][] derivativeOfTheRotationMatrix, double[] momentOfForce) {
        this.centerOfMass = centerOfMass;
        this.momentum = momentum;
        this.rotationMatrix = rotationMatrix;
        this.angularMomentum = angularMomentum;
        this.velocity = velocity;
        this.force = force;
        this.derivativeOfTheRotationMatrix = derivativeOfTheRotationMatrix;
        this.momentOfForce = momentOfForce;
    }

    public State(double[] centerOfMass, double[] momentum, double[] angularMomentum, double[] velocity, double[] force, double[][] derivativeOfTheRotationMatrix, double[] momentOfForce) {
        this.rotationMatrix = new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        this.centerOfMass = centerOfMass;
        this.momentum = momentum;
        this.angularMomentum = angularMomentum;
        this.velocity = velocity;
        this.force = force;
        this.derivativeOfTheRotationMatrix = derivativeOfTheRotationMatrix;
        this.momentOfForce = momentOfForce;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public void setCenterOfMass(double[] centerOfMass) {
        this.centerOfMass = centerOfMass;
    }

    public void setMomentum(double[] momentum) {
        this.momentum = momentum;
    }

    public void setRotationMatrix(double[][] rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }

    public void setAngularMomentum(double[] angularMomentum) {
        this.angularMomentum = angularMomentum;
    }

    public void setForce(double[] force) {
        this.force = force;
    }

    public void setDerivativeOfTheRotationMatrix(double[][] derivativeOfTheRotationMatrix) {
        this.derivativeOfTheRotationMatrix = derivativeOfTheRotationMatrix;
    }

    public void setMomentOfForce(double[] momentOfForce) {
        this.momentOfForce = momentOfForce;
    }

    public double[] getCenterOfMass() {
        return centerOfMass;
    }

    public double[] getMomentum() {
        return momentum;
    }

    public double[][] getRotationMatrix() {
        return rotationMatrix;
    }

    public double[] getAngularMomentum() {
        return angularMomentum;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public double[] getForce() {
        return force;
    }

    public double[][] getDerivativeOfTheRotationMatrix() {
        return derivativeOfTheRotationMatrix;
    }

    public double[] getMomentOfForce() {
        return momentOfForce;
    }

    @Override
    public State clone() {
        return new State(
                this.centerOfMass.clone(),
                this.momentum.clone(),
                deepCopyMatrix(this.rotationMatrix),
                this.angularMomentum.clone(),
                this.velocity.clone(),
                this.force != null ? this.force.clone() : null,
                this.derivativeOfTheRotationMatrix.clone(),
                this.momentOfForce != null ? this.momentOfForce.clone() : null
        );
    }

    private static double[][] deepCopyMatrix(double[][] m) {
        if (m == null) return null;
        double[][] copy = new double[m.length][];
        for (int i = 0; i < m.length; i++) {
            copy[i] = m[i].clone();
        }
        return copy;
    }


    public State add(Derivatives k, double h) {
        double[] newCenterOfMass = VectorUtils.vectorSum(this.centerOfMass,
                VectorUtils.multiplyByScalar(k.dCenterOfMass, h));
        double[] newMomentum = VectorUtils.vectorSum(this.momentum,
                VectorUtils.multiplyByScalar(k.dMomentum, h));
        double[] newAngularMomentum = VectorUtils.vectorSum(this.angularMomentum,
                VectorUtils.multiplyByScalar(k.dAngularMomentum, h));
        double[][] newRotationMatrix = MatrixUtils.addMatrices(this.rotationMatrix,
                MatrixUtils.multiplyByScalar(k.dRotationMatrix, h));

        return new State(
                newCenterOfMass,
                newMomentum,
                newRotationMatrix,
                newAngularMomentum,
                this.velocity,
                this.force != null ? this.force.clone() : null,
                this.derivativeOfTheRotationMatrix != null ? deepCopyMatrix(this.derivativeOfTheRotationMatrix) : null,
                this.momentOfForce != null ? this.momentOfForce.clone() : null
        );
    }


    private static double[] add(double[] a, double[] b) {
        double[] r = new double[a.length];
        for (int i = 0; i < a.length; i++) r[i] = a[i] + b[i];
        return r;
    }

    private static double[][] add(double[][] a, double[][] b) {
        double[][] r = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++)
                r[i][j] = a[i][j] + b[i][j];
        return r;
    }

    private static double[] scale(double[] a, double s) {
        double[] r = new double[a.length];
        for (int i = 0; i < a.length; i++) r[i] = a[i] * s;
        return r;
    }

    private static double[][] scale(double[][] a, double s) {
        double[][] r = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++)
                r[i][j] = a[i][j] * s;
        return r;
    }
}
