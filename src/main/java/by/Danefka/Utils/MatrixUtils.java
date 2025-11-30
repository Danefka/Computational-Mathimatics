package by.Danefka.Utils;

public class MatrixUtils {

    // Сложение матриц
    public static double[][] addMatrices(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }

        return result;
    }

    // Умножение матрицы 3x3 на вектор 3
    public static double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            result[i] = 0;
            for (int j = 0; j < 3; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    // Нахождение обратной матрицы 3x3
    public static double[][] invert3x3Matrix(double[][] m) {
        double det = m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1]) -
                     m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0]) +
                     m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);

        if (Math.abs(det) < 1e-10) {
            throw new IllegalArgumentException("Матрица вырожденная, обратной не существует");
        }

        double[][] inv = new double[3][3];
        inv[0][0] =  (m[1][1] * m[2][2] - m[1][2] * m[2][1]) / det;
        inv[0][1] = -(m[0][1] * m[2][2] - m[0][2] * m[2][1]) / det;
        inv[0][2] =  (m[0][1] * m[1][2] - m[0][2] * m[1][1]) / det;
        inv[1][0] = -(m[1][0] * m[2][2] - m[1][2] * m[2][0]) / det;
        inv[1][1] =  (m[0][0] * m[2][2] - m[0][2] * m[2][0]) / det;
        inv[1][2] = -(m[0][0] * m[1][2] - m[0][2] * m[1][0]) / det;
        inv[2][0] =  (m[1][0] * m[2][1] - m[1][1] * m[2][0]) / det;
        inv[2][1] = -(m[0][0] * m[2][1] - m[0][1] * m[2][0]) / det;
        inv[2][2] =  (m[0][0] * m[1][1] - m[0][1] * m[1][0]) / det;

        return inv;
    }


    // Транспонирование матрицы
    public static double[][] transpose(double[][] m) {
        int rows = m.length;
        int cols = m[0].length;

        double[][] t = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                t[j][i] = m[i][j];
            }
        }

        return t;
    }

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
    public static double[][] gramSchmidt(double[][] R) {
        double[][] ortho = new double[3][3];

        double[] c0 = { R[0][0], R[1][0], R[2][0] };
        double[] c1 = { R[0][1], R[1][1], R[2][1] };
        double[] c2 = { R[0][2], R[1][2], R[2][2] };

        normalize(c0);
        subtractProjection(c1, c0);
        normalize(c1);
        subtractProjection(c2, c0);
        subtractProjection(c2, c1);
        normalize(c2);

        for (int i = 0; i < 3; i++) {
            ortho[i][0] = c0[i];
            ortho[i][1] = c1[i];
            ortho[i][2] = c2[i];
        }

        return ortho;
    }

    private static void normalize(double[] v) {
        double norm = Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
        if (norm < 1e-8) norm = 1e-8;
        v[0] /= norm;
        v[1] /= norm;
        v[2] /= norm;
    }

    private static void subtractProjection(double[] v, double[] u) {
        double dot = v[0]*u[0] + v[1]*u[1] + v[2]*u[2];
        v[0] -= dot * u[0];
        v[1] -= dot * u[1];
        v[2] -= dot * u[2];
    }
}
