package by.Danefka.Utils;

import java.util.Arrays;

public class MatrixUtils {

    // Умножение матрицы на число
    public static double[][] multiplyByScalar(double[][] matrix, double scalar) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix[i][j] * scalar;
            }
        }
        return result;
    }

    //  Умножение матрицы на матрицу
    public static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int cols2 = matrix2[0].length;

        if (cols1 != rows2) {
            throw new IllegalArgumentException("Матрицы нельзя перемножить");
        }

        double[][] result = new double[rows1][cols2];

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    // Ортогонализация матрицы методом Грама-Шмидта
    public static double[][] gramSchmidtOrthogonalization(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] orthoMatrix = new double[rows][cols];

        for (int j = 0; j < cols; j++) {
            double[] v = Arrays.copyOf(matrix[j], rows);

            for (int k = 0; k < j; k++) {
                double dotProduct = 0;
                double normSquared = 0;

                for (int i = 0; i < rows; i++) {
                    dotProduct += matrix[j][i] * orthoMatrix[k][i];
                    normSquared += orthoMatrix[k][i] * orthoMatrix[k][i];
                }

                double scalar = dotProduct / normSquared;
                for (int i = 0; i < rows; i++) {
                    v[i] -= scalar * orthoMatrix[k][i];
                }
            }
            orthoMatrix[j] = v;
        }
        return orthoMatrix;
    }
}
