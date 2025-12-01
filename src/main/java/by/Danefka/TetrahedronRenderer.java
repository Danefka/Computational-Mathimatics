package by.Danefka;

import by.Danefka.Solids.Tetrahedron;
import by.Danefka.Solids.Face;
import org.lwjgl.BufferUtils;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class TetrahedronRenderer {
    public void render(Tetrahedron tetrahedron, State state) {
        double[][] vertices = tetrahedron.getVertices();
        Face[] faces = tetrahedron.getFaces();

        double[] center = state.getCenterOfMass();
        double[][] rot = state.getRotationMatrix();

        FloatBuffer transformBuf = BufferUtils.createFloatBuffer(16);
        Matrix4f transform = new Matrix4f(
                (float) rot[0][0], (float) rot[0][1], (float) rot[0][2], 0f,
                (float) rot[1][0], (float) rot[1][1], (float) rot[1][2], 0f,
                (float) rot[2][0], (float) rot[2][1], (float) rot[2][2], 0f,
                (float) center[0], (float) center[1], (float) center[2], 1f
        );
        transform.get(transformBuf);
        transformBuf.rewind();

        glPushMatrix();
        glMultMatrixf(transformBuf);

        drawTetrahedron(faces, vertices);

        glPopMatrix();
    }

    private void drawTetrahedron(Face[] faces, double[][] vertices) {
        glBegin(GL_TRIANGLES);
        for (Face face : faces) {
            int[] idx = face.getVerts();
            double[] normal = face.getNorm();

            glNormal3d(normal[0], normal[1], normal[2]);
            glColor3f(0.7f, 0.7f, 1.0f);

            for (int i : idx) {
                double[] v = vertices[i];
                glVertex3d(v[0], v[1], v[2]);
            }
        }
        glEnd();

        glColor3f(0.0f, 0.0f, 0.0f);
        glLineWidth(1.2f);
        glBegin(GL_LINES);
        for (Face face : faces) {
            int[] idx = face.getVerts();
            for (int i = 0; i < 3; i++) {
                double[] v1 = vertices[idx[i]];
                double[] v2 = vertices[idx[(i + 1) % 3]];
                glVertex3d(v1[0], v1[1], v1[2]);
                glVertex3d(v2[0], v2[1], v2[2]);
            }
        }
        glEnd();
    }
}
