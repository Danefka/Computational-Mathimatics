package by.Danefka;

import by.Danefka.Solids.Polyhedron;
import by.Danefka.Solids.PolyhedronClipper;
import by.Danefka.methods.ICalculate;

import java.util.Arrays;

public class DrownCalculator {
    private double h;

    public DrownCalculator(double h) {
        this.h = h;
    }

    public State calculate(ICalculate function, State state, Polyhedron polyhedron) {
        state = function.calculate(state, h);
        return state;
    }

    public State archimedesForceCalculate(State state, Polyhedron polyhedron) {
        double[][] vertices = polyhedron.getVertices();
        double[][] rotatedVertices = new double[vertices.length][3];
        double[][] rotationMatrix = state.getRotationMatrix();

        boolean hasVertUnderZero = false;
        boolean hasVertAboveZero = false;
        for (int i = 0; i < vertices.length; i++) {
            double x = vertices[i][0];
            double y = vertices[i][1];
            double z = vertices[i][2];

            // Умножение матрицы поворота на вектор (x, y, z)
            rotatedVertices[i][0] = rotationMatrix[0][0] * x + rotationMatrix[0][1] * y + rotationMatrix[0][2] * z;
            rotatedVertices[i][1] = rotationMatrix[1][0] * x + rotationMatrix[1][1] * y + rotationMatrix[1][2] * z;
            rotatedVertices[i][2] = rotationMatrix[2][0] * x + rotationMatrix[2][1] * y + rotationMatrix[2][2] * z;
            if (!hasVertAboveZero || !hasVertUnderZero) {
                if (rotatedVertices[i][2] < 0) {
                    hasVertUnderZero = true;
                } else if (rotatedVertices[i][2] > 0) {
                    hasVertAboveZero = true;
                }
            }
        }

        if (hasVertAboveZero && hasVertUnderZero) {
            Polyhedron polyhedronClipper = PolyhedronClipper.clipPolyhedron(polyhedron);
            System.out.println(Arrays.toString(polyhedronClipper.getCenterMass()));
            System.out.println(Arrays.deepToString(polyhedronClipper.getVertices()));
            System.out.println(Arrays.toString(polyhedronClipper.getFaces()));

        } else if (hasVertAboveZero) {
            /*free fall*/
            return state;
        }
        /*full polyhedron is underwater*/
        return state;

    }
}
