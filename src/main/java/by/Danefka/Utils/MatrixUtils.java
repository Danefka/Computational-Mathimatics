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
    public static double[][] gramSchmidt(double[][] vectors) {
        int n = vectors.length;
        int m = vectors[0].length;

        double[][] ortho = new double[n][m];

        for (int k = 0; k < n; k++) {
            // берём очередной вектор v_k
            double[] vk = vectors[k].clone();

            // вычитаем проекции на уже построенные ортонормированные векторы
            for (int j = 0; j < k; j++) {
                double dot = 0.0;
                double norm2 = 0.0;
                for (int i = 0; i < m; i++) {
                    dot   += vk[i] * ortho[j][i];
                    norm2 += ortho[j][i] * ortho[j][i];
                }
                double coeff = dot / norm2;
                for (int i = 0; i < m; i++) {
                    vk[i] -= coeff * ortho[j][i];
                }
            }

            // нормируем полученный вектор
            double norm = 0.0;
            for (int i = 0; i < m; i++) {
                norm += vk[i] * vk[i];
            }
            norm = Math.sqrt(norm);
            if (norm == 0.0) {
                throw new IllegalArgumentException("Векторы линейно зависимы");
            }
            for (int i = 0; i < m; i++) {
                ortho[k][i] = vk[i] / norm;
            }
        }
        return ortho;
    }
}
