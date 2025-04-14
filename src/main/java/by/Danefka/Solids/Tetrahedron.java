package by.Danefka.Solids;

public class Tetrahedron extends Polyhedron {


    public Tetrahedron(double[][] vertices, double density) {
        super(vertices, createTetrahedronFaces(vertices), density);
    }

    private static Face[] createTetrahedronFaces(double[][] vertices) {
        if (vertices.length != 4) {
            throw new IllegalArgumentException("Тетраэдр должен иметь 4 вершины");
        }

        // Индексы вершин для каждой грани (исключая одну вершину)
        int[][] faceIndices = {
                {0, 1, 2},
                {0, 1, 3},
                {0, 2, 3},
                {1, 2, 3}
        };

        Face[] faces = new Face[4];

        for (int i = 0; i < 4; i++) {
            int[] indices = faceIndices[i];
            int excludedVertex = 6 - indices[0] - indices[1] - indices[2]; // 0+1+2+3=6

            // Получаем вершины грани
            double[] A = vertices[indices[0]];
            double[] B = vertices[indices[1]];
            double[] C = vertices[indices[2]];
            double[] D = vertices[excludedVertex];

            // Вычисляем нормаль грани
            double[] normal = computeNormal(A, B, C);

            // Проверяем направление нормали относительно исключенной вершины
            if (isNormalPointingInward(A, B, C, D, normal)) {
                // Меняем порядок вершин для корректировки нормали
                int temp = indices[1];
                indices[1] = indices[2];
                indices[2] = temp;
            }

            // Создаем грань с правильным порядком вершин
            faces[i] = createFace(vertices, indices);
        }

        return faces;
    }

    // Вычисляет нормаль грани (A, B, C)
    private static double[] computeNormal(double[] A, double[] B, double[] C) {
        double[] AB = {B[0] - A[0], B[1] - A[1], B[2] - A[2]};
        double[] AC = {C[0] - A[0], C[1] - A[1], C[2] - A[2]};

        // Векторное произведение AB × AC
        double[] normal = {
                AB[1] * AC[2] - AB[2] * AC[1],
                AB[2] * AC[0] - AB[0] * AC[2],
                AB[0] * AC[1] - AB[1] * AC[0]
        };

        // Нормализация (опционально)
        double len = Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
        if (len > 0) {
            normal[0] /= len;
            normal[1] /= len;
            normal[2] /= len;
        }

        return normal;
    }

    // Проверяет, направлена ли нормаль внутрь тетраэдра
    private static boolean isNormalPointingInward(double[] A, double[] B, double[] C, double[] D, double[] normal) {
        // Центр грани
        double[] center = {
                (A[0] + B[0] + C[0]) / 3,
                (A[1] + B[1] + C[1]) / 3,
                (A[2] + B[2] + C[2]) / 3
        };

        // Вектор от центра грани к вершине D
        double[] vecToD = {D[0] - center[0], D[1] - center[1], D[2] - center[2]};

        // Скалярное произведение нормали и вектора к D
        double dot = normal[0] * vecToD[0] + normal[1] * vecToD[1] + normal[2] * vecToD[2];

        // Если скалярное произведение положительно, нормаль направлена к D (внутрь)
        return dot > 0;
    }

    // Создает объект Face с указанными вершинами
    private static Face createFace(double[][] vertices, int[] indices) {
        int[] verts = new int[3];
        verts[0] = indices[0];
        verts[1] = indices[1];
        verts[2] = indices[2];

        // Вычисляем нормаль и параметр плоскости
        double[] A = vertices[indices[0]];
        double[] B = vertices[indices[1]];
        double[] C = vertices[indices[2]];
        double[] normal = computeNormal(A, B, C);
        double w = -(normal[0] * A[0] + normal[1] * A[1] + normal[2] * A[2]);

        return new Face(3,normal,w,verts);
    }
}





