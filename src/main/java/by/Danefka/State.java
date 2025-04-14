package by.Danefka;

public class State {
    private double[] centerOfMass;
    private double[] momentum;
    private double[][] rotationMatrix;
    private double[] angularMomentum;

    private double[] velocity;
    private double[] force;
    private double[][] derivativeOfTheRotationMatrix;
    private double[] momentOfForce;

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
}
