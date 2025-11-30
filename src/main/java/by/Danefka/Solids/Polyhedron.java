package by.Danefka.Solids;

import by.Danefka.State;
import by.Danefka.Utils.MatrixUtils;
import by.Danefka.Utils.VectorUtils;

import static by.Danefka.Utils.NumberUtils.cube;
import static by.Danefka.Utils.NumberUtils.sqr;

public class Polyhedron {
    protected double[][] vertices;
    protected Face[] faces;
    protected double density;

    private final double[] centerMass = new double[3];
    private double volume;
    private double mass;
    private final double[][] inertiaTensor = new double[3][3];
    private double[][] invInertiaTensor = new double[3][3];
    private double[][] worldVerts;

    public Polyhedron(double[][] vertices, Face[] faces, double density, double volume, double mass, double[][] invInertiaTensor, double[][] worldVerts) {
        this.vertices = vertices;
        this.faces = faces;
        this.density = density;
        this.volume = volume;
        this.mass = mass;
        this.invInertiaTensor = invInertiaTensor;
        this.worldVerts = worldVerts;
    }

    public Polyhedron(double[][] vertices, Face[] faces, double density) {
        this.vertices = vertices;
        this.faces = faces;
        this.density = density;
        initPolyhedron(density);

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = VectorUtils.vectorSub(vertices[i], centerMass);
        }
        worldVerts = new double[vertices.length][3];
        for (int i = 0; i < vertices.length; i++) {
            worldVerts[i][X] = vertices[i][X];
            worldVerts[i][Y] = vertices[i][Y];
            worldVerts[i][Z] = vertices[i][Z];

        }
        invInertiaTensor = MatrixUtils.invert3x3Matrix(inertiaTensor);
    }

    private void initPolyhedron(double density) {
        compVolumeIntegrals();
        volume = T0;
        mass = density * T0;


        centerMass[X] = T1x / T0;
        centerMass[Y] = T1y / T0;
        centerMass[Z] = T1z / T0;

        inertiaTensor[X][X] = density * (T2y + T2z);
        inertiaTensor[Y][Y] = density * (T2z + T2x);
        inertiaTensor[Z][Z] = density * (T2x + T2y);
        inertiaTensor[X][Y] = inertiaTensor[Y][X] = -density * TPx;
        inertiaTensor[Y][Z] = inertiaTensor[Z][Y] = -density * TPy;
        inertiaTensor[Z][X] = inertiaTensor[X][Z] = -density * TPz;

        // Перенос тензора инерции в систему координат, центрированную в центре масс
        inertiaTensor[X][X] -= mass * (sqr(centerMass[Y]) + sqr(centerMass[Z]));
        inertiaTensor[Y][Y] -= mass * (sqr(centerMass[Z]) + sqr(centerMass[X]));
        inertiaTensor[Z][Z] -= mass * (sqr(centerMass[X]) + sqr(centerMass[Y]));
        inertiaTensor[X][Y] = inertiaTensor[Y][X] += mass * centerMass[X] * centerMass[Y];
        inertiaTensor[Y][Z] = inertiaTensor[Z][Y] += mass * centerMass[Y] * centerMass[Z];
        inertiaTensor[Z][X] = inertiaTensor[X][Z] += mass * centerMass[Z] * centerMass[X];
    }

    /*Код Мартича для нахождения физических характеристик тела
     * https://github.com/OpenFOAM/OpenFOAM-4.x/blob/master/src/meshTools/momentOfInertia/volumeIntegration/volInt.c
     * */

    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    /* Глобальные переменные для выбора проекции */
    private int A;   // альфа
    private int B;   // бета
    private int C;   // гамма

    /* Переменные для проекционных интегралов */
    private double P1, Pa, Pb, Paa, Pab, Pbb, Paaa, Paab, Pabb, Pbbb;

    /* Переменные для интегралов по граням */
    private double Fa, Fb, Fc, Faa, Fbb, Fcc, Faaa, Fbbb, Fccc, Faab, Fbbc, Fcca;

    /* Переменные для объемных интегралов */
    private double T0, T1x, T1y, T1z;
    private double T2x, T2y, T2z;
    private double TPx, TPy, TPz;

    /* Вычисление проекционных интегралов по грани */
    private void compProjectionIntegrals(Face f) {
        P1 = Pa = Pb = Paa = Pab = Pbb = Paaa = Paab = Pabb = Pbbb = 0.0;

        for (int i = 0; i < f.numVerts; i++) {
            double a0 = vertices[f.verts[i]][A];
            double b0 = vertices[f.verts[i]][B];
            double a1 = vertices[f.verts[(i + 1) % f.numVerts]][A];
            double b1 = vertices[f.verts[(i + 1) % f.numVerts]][B];
            double da = a1 - a0;
            double db = b1 - b0;
            double a0_2 = a0 * a0, a0_3 = a0_2 * a0, a0_4 = a0_3 * a0;
            double b0_2 = b0 * b0, b0_3 = b0_2 * b0, b0_4 = b0_3 * b0;
            double a1_2 = a1 * a1, a1_3 = a1_2 * a1;
            double b1_2 = b1 * b1, b1_3 = b1_2 * b1;

            double C1 = a1 + a0;
            double Ca = a1 * C1 + a0_2;
            double Caa = a1 * Ca + a0_3;
            double Caaa = a1 * Caa + a0_4;
            double Cb = b1 * (b1 + b0) + b0_2;
            double Cbb = b1 * Cb + b0_3;
            double Cbbb = b1 * Cbb + b0_4;
            double Cab = 3 * a1_2 + 2 * a1 * a0 + a0_2;
            double Kab = a1_2 + 2 * a1 * a0 + 3 * a0_2;
            double Caab = a0 * Cab + 4 * a1_3;
            double Kaab = a1 * Kab + 4 * a0_3;
            double Cabb = 4 * b1_3 + 3 * b1_2 * b0 + 2 * b1 * b0_2 + b0_3;
            double Kabb = b1_3 + 2 * b1_2 * b0 + 3 * b1 * b0_2 + 4 * b0_3;

            P1 += db * C1;
            Pa += db * Ca;
            Paa += db * Caa;
            Paaa += db * Caaa;
            Pb += da * Cb;
            Pbb += da * Cbb;
            Pbbb += da * Cbbb;
            Pab += db * (b1 * Cab + b0 * Kab);
            Paab += db * (b1 * Caab + b0 * Kaab);
            Pabb += da * (a1 * Cabb + a0 * Kabb);
        }

        P1 /= 2.0;
        Pa /= 6.0;
        Paa /= 12.0;
        Paaa /= 20.0;
        Pb /= -6.0;
        Pbb /= -12.0;
        Pbbb /= -20.0;
        Pab /= 24.0;
        Paab /= 60.0;
        Pabb /= -60.0;
    }

    /* Вычисление интегралов по грани */
    private void compFaceIntegrals(Face f) {
        compProjectionIntegrals(f);

        double w = f.w;
        double[] n = f.norm;
        double k1 = 1.0 / n[C];
        double k2 = k1 * k1;
        double k3 = k2 * k1;
        double k4 = k3 * k1;

        Fa = k1 * Pa;
        Fb = k1 * Pb;
        Fc = -k2 * (n[A] * Pa + n[B] * Pb + w * P1);

        Faa = k1 * Paa;
        Fbb = k1 * Pbb;
        Fcc = k3 * (sqr(n[A]) * Paa + 2 * n[A] * n[B] * Pab + sqr(n[B]) * Pbb
                    + w * (2 * (n[A] * Pa + n[B] * Pb) + w * P1));

        Faaa = k1 * Paaa;
        Fbbb = k1 * Pbbb;
        Fccc = -k4 * (cube(n[A]) * Paaa + 3 * sqr(n[A]) * n[B] * Paab
                      + 3 * n[A] * sqr(n[B]) * Pabb + cube(n[B]) * Pbbb
                      + 3 * w * (sqr(n[A]) * Paa + 2 * n[A] * n[B] * Pab + sqr(n[B]) * Pbb)
                      + w * w * (3 * (n[A] * Pa + n[B] * Pb) + w * P1));

        Faab = k1 * Paab;
        Fbbc = -k2 * (n[A] * Pabb + n[B] * Pbbb + w * Pbb);
        Fcca = k3 * (sqr(n[A]) * Paaa + 2 * n[A] * n[B] * Paab + sqr(n[B]) * Pabb
                     + w * (2 * (n[A] * Paa + n[B] * Pab) + w * Pa));
    }

    /* Вычисление объемных интегралов для многоугольного тела */
    private void compVolumeIntegrals() {
        T0 = T1x = T1y = T1z = 0;
        T2x = T2y = T2z = 0;
        TPx = TPy = TPz = 0;

        for (int i = 0; i < faces.length; i++) {
            Face f = faces[i];

            double nx = Math.abs(f.norm[X]);
            double ny = Math.abs(f.norm[Y]);
            double nz = Math.abs(f.norm[Z]);
            if (nx > ny && nx > nz) {
                C = X;
            } else {
                C = (ny > nz) ? Y : Z;
            }
            A = (C + 1) % 3;
            B = (A + 1) % 3;

            compFaceIntegrals(f);

            // Суммируем интегралы
            double faceContribution;
            if (A == X) {
                faceContribution = Fa;
            } else if (B == X) {
                faceContribution = Fb;
            } else {
                faceContribution = Fc;
            }
            T0 += f.norm[X] * faceContribution;

            T1x += f.norm[A] * Faa * ((A == X) ? 1 : 0)
                   + f.norm[B] * Fbb * ((B == X) ? 1 : 0)
                   + f.norm[C] * Fcc * ((C == X) ? 1 : 0);
            T1y += f.norm[A] * Faa * ((A == Y) ? 1 : 0)
                   + f.norm[B] * Fbb * ((B == Y) ? 1 : 0)
                   + f.norm[C] * Fcc * ((C == Y) ? 1 : 0);
            T1z += f.norm[A] * Faa * ((A == Z) ? 1 : 0)
                   + f.norm[B] * Fbb * ((B == Z) ? 1 : 0)
                   + f.norm[C] * Fcc * ((C == Z) ? 1 : 0);

            T2x += f.norm[A] * Faaa * ((A == X) ? 1 : 0)
                   + f.norm[B] * Fbbb * ((B == X) ? 1 : 0)
                   + f.norm[C] * Fccc * ((C == X) ? 1 : 0);
            T2y += f.norm[A] * Faaa * ((A == Y) ? 1 : 0)
                   + f.norm[B] * Fbbb * ((B == Y) ? 1 : 0)
                   + f.norm[C] * Fccc * ((C == Y) ? 1 : 0);
            T2z += f.norm[A] * Faaa * ((A == Z) ? 1 : 0)
                   + f.norm[B] * Fbbb * ((B == Z) ? 1 : 0)
                   + f.norm[C] * Fccc * ((C == Z) ? 1 : 0);

            TPx += f.norm[A] * Faab * ((A == X) ? 1 : 0)
                   + f.norm[B] * Fbbc * ((B == X) ? 1 : 0)
                   + f.norm[C] * Fcca * ((C == X) ? 1 : 0);
            TPy += f.norm[A] * Faab * ((A == Y) ? 1 : 0)
                   + f.norm[B] * Fbbc * ((B == Y) ? 1 : 0)
                   + f.norm[C] * Fcca * ((C == Y) ? 1 : 0);
            TPz += f.norm[A] * Faab * ((A == Z) ? 1 : 0)
                   + f.norm[B] * Fbbc * ((B == Z) ? 1 : 0)
                   + f.norm[C] * Fcca * ((C == Z) ? 1 : 0);
        }

        T1x /= 2.0;
        T1y /= 2.0;
        T1z /= 2.0;
        T2x /= 3.0;
        T2y /= 3.0;
        T2z /= 3.0;
        TPx /= 2.0;
        TPy /= 2.0;
        TPz /= 2.0;
    }

    public double[] getCenterMass() {
        return centerMass;
    }

    public double getVolume() {
        return volume;
    }

    public double[][] getInertiaTensor() {
        return inertiaTensor;
    }

    public double[][] getVertices() {
        return vertices;
    }

    public Face[] getFaces() {
        return faces;
    }

    public double getDensity() {
        return density;
    }

    public double[][] getInvInertiaTensor() {
        return invInertiaTensor;
    }

    public void updateWorldVertices(State state) {
        if (worldVerts == null) {
            worldVerts = new double[vertices.length][3];
        }

        double[][] rot = state.getRotationMatrix();
        double[] com = state.getCenterOfMass();

        for (int i = 0; i < vertices.length; i++) {
            double[] v = vertices[i]; // локальная вершина
            worldVerts[i][0] = rot[0][0]*v[0] + rot[0][1]*v[1] + rot[0][2]*v[2] + com[0];
            worldVerts[i][1] = rot[1][0]*v[0] + rot[1][1]*v[1] + rot[1][2]*v[2] + com[1];
            worldVerts[i][2] = rot[2][0]*v[0] + rot[2][1]*v[1] + rot[2][2]*v[2] + com[2];
        }
    }

    @Override
    public Polyhedron clone() {
        Polyhedron copy = new Polyhedron(
                deepCopy(vertices),
                faces.clone(),
                density,
                volume,
                mass,
                deepCopy(invInertiaTensor),
                worldVerts != null ? deepCopy(worldVerts) : null
        );

        copyCenterMass(copy.centerMass, this.centerMass);
        copyMatrix(copy.inertiaTensor, this.inertiaTensor);

        return copy;
    }


    private static double[][] deepCopy(double[][] src) {
        if (src == null) return null;
        double[][] dst = new double[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = src[i].clone();
        }
        return dst;
    }

    private static void copyCenterMass(double[] dst, double[] src) {
        System.arraycopy(src, 0, dst, 0, 3);
    }

    private static void copyMatrix(double[][] dst, double[][] src) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, 3);
        }
    }




    public double[][] getWorldVerts(){
        return worldVerts;
    }
}
