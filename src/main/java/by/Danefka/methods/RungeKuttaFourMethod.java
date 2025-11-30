package by.Danefka.methods;

import by.Danefka.Calculations.CalculateForces;
import by.Danefka.Calculations.CalculateMomentOfForce;
import by.Danefka.Solids.Polyhedron;
import by.Danefka.State;
import by.Danefka.Utils.MatrixUtils;
import by.Danefka.Utils.VectorUtils;

import java.util.Arrays;

public class RungeKuttaFourMethod implements ICalculate {

    private final double h = 0.1;

    private final CalculateForces calculateForces = new CalculateForces();
    private final CalculateMomentOfForce calculateMomentOfForce = new CalculateMomentOfForce();

    @Override
    public State calculate(State s, Polyhedron p) {
        for (int i = 0; i < 4; i++) {
            System.out.println(Arrays.toString(p.getWorldVerts()[i]));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(s.getRotationMatrix()[i][j]);
            }
        }

        Derivatives k1 = derivatives(s, p);
        State s1 = s.clone().add(k1, h/2);
        Polyhedron pTemp = p.clone();
        pTemp.updateWorldVertices(s1);
        for (int i = 0; i < 4; i++) {
            System.out.println(Arrays.toString(pTemp.getWorldVerts()[i]));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(s1.getRotationMatrix()[i][j]);
            }
        }

        Derivatives k2 = derivatives(s1,pTemp);
        State s2 = s.clone().add(k2, h/2);
        pTemp = p.clone();
        pTemp.updateWorldVertices(s2);
        for (int i = 0; i < 4; i++) {
            System.out.println(Arrays.toString(pTemp.getWorldVerts()[i]));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(s2.getRotationMatrix()[i][j]);
            }
        }

        Derivatives k3 = derivatives(s2, pTemp);
        State s3 = s.clone().add(k3, h);
        pTemp = p.clone();
        pTemp.updateWorldVertices(s3);
        for (int i = 0; i < 4; i++) {
            System.out.println(Arrays.toString(pTemp.getWorldVerts()[i]));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(s3.getRotationMatrix()[i][j]);
            }
        }

        Derivatives k4 = derivatives(s3, pTemp);

        Derivatives sum = k1.scale(1.0/6)
                .add(k2.scale(1.0/3))
                .add(k3.scale(1.0/3))
                .add(k4.scale(1.0/6));


        double[] newCenterOfMass = VectorUtils.vectorSum(s.getCenterOfMass(),VectorUtils.multiplyByScalar(sum.dCenterOfMass,h));
        double[] newMomentum = VectorUtils.vectorSum(s.getMomentum(),VectorUtils.multiplyByScalar(sum.dMomentum,h));
        double[][] newRotationMatrix = MatrixUtils.gramSchmidt(MatrixUtils.addMatrices(s.getRotationMatrix(),MatrixUtils.multiplyByScalar(sum.dRotationMatrix, h)));
        double[] newAngularMomentum = VectorUtils.vectorSum(s.getAngularMomentum(),VectorUtils.multiplyByScalar(sum.dAngularMomentum,h));

        return new State(
                newCenterOfMass,
                newMomentum,
                newRotationMatrix,
                newAngularMomentum,
                VectorUtils.multiplyByScalar(newMomentum, 1/(p.getVolume()*p.getDensity())),
                s.getForce(),
                newRotationMatrix(p,newAngularMomentum,newRotationMatrix),
                s.getMomentOfForce()
        );
    }

    private Derivatives derivatives(State s, Polyhedron p) {
        double[] force = calculateForces.calculate(p);
        double[] momentOfForce = calculateMomentOfForce.calculate(p);


        double[] dCenterMass = s.getVelocity();
        double[][] dRotationMatrix = dRotationCalculate(s, p);

        return new Derivatives(
                dCenterMass, force, momentOfForce,dRotationMatrix
        );
    }

    private double[][] newRotationMatrix(Polyhedron p, double[] newAngularMomentum, double[][] newRotationMatrix){
        double[][] R = newRotationMatrix;
        double[] L = newAngularMomentum;

        double[][] Rt = MatrixUtils.transpose(R);
        double[] L_body = MatrixUtils.multiplyMatrixVector(Rt, L);

        double[] omega_body = MatrixUtils.multiplyMatrixVector(p.getInvInertiaTensor(), L_body);

        double[] omega = MatrixUtils.multiplyMatrixVector(R, omega_body);

        double[][] Omega = {
                {0,         -omega[2],  omega[1]},
                {omega[2],   0,        -omega[0]},
                {-omega[1],  omega[0],  0}
        };

        return MatrixUtils.multiplyMatrices(Omega, newRotationMatrix);
    }

    private double[][] dRotationCalculate(State s, Polyhedron p) {
        double[][] R = s.getRotationMatrix();
        double[] L = s.getAngularMomentum();

        double[][] Rt = MatrixUtils.transpose(R);
        double[] L_body = MatrixUtils.multiplyMatrixVector(Rt, L);

        double[] omega_body = MatrixUtils.multiplyMatrixVector(p.getInvInertiaTensor(), L_body);

        double[] omega = MatrixUtils.multiplyMatrixVector(R, omega_body);

        double[][] Omega = {
                {0, -omega[2], omega[1]},
                {omega[2], 0, -omega[0]},
                {-omega[1], omega[0], 0}
        };

        return MatrixUtils.multiplyMatrices(Omega, R);
    }

}
