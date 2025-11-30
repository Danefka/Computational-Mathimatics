package by.Danefka;

import by.Danefka.Solids.Tetrahedron;
import by.Danefka.methods.RungeKuttaFourMethod;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLIceberg {

    private long window;
    private final int width = 1200;
    private final int height = 800;

    private RungeKuttaFourMethod integrator;
    private State state;
    private Tetrahedron tetra;

    private TetrahedronRenderer renderer;

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) callback.free();
    }

    private void init() {
        double[][] verts = {
                {0, 0, 0},
                {10, 0, 0},
                {0.5, Math.sqrt(3)/2, 0},
                {0.5, Math.sqrt(3)/6, Math.sqrt(6)/3}
        };
        tetra = new Tetrahedron(verts, 999);

        state = new State();
        state.setCenterOfMass(new double[]{0.0, 0.0, 1.5});
        state.setMomentum(new double[]{0,0,0});
        state.setAngularMomentum(new double[]{10,0.2,0.3});
        state.setVelocity(new double[]{0,0,0});
        state.setForce(new double[]{0,0,0});
        state.setMomentOfForce(new double[]{0,0,0});
        state.setRotationMatrix(new double[][] {
                {1,0,0},
                {0,1,0},
                {0,0,1}
        });

        integrator = new RungeKuttaFourMethod();
        renderer = new TetrahedronRenderer();

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Не удалось инициализировать GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, "Iceberg Simulation", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Ошибка создания окна");

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void setupFixedFunctionState() {
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_NORMALIZE);

        glClearColor(0.2f, 0.3f, 0.4f, 1.0f);
    }

    private void loop() {
        setupFixedFunctionState();

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            // 1. Интегрируем состояние
            state = integrator.calculate(state, tetra);

            // 2. Обновляем мировые координаты тетраэдра
            tetra.updateWorldVertices(state);
            for (int i = 0; i < 4; i++) {
                System.out.println(Arrays.toString(tetra.getWorldVerts()[i]));
            }

            // 3. Рисуем сцену
            draw();
            while (true){
                int i = 0;
            }
//            glfwSwapBuffers(window);
        }
    }

    private void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Projection
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        Matrix4f projection = new Matrix4f().perspective(
                (float) Math.toRadians(60),
                (float) width / height,
                0.1f,
                100.0f
        );
        FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
        projection.get(projBuf).rewind();
        glLoadMatrixf(projBuf);

        // View
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        Matrix4f view = new Matrix4f().lookAt(
                30.0f, 30.0f, 20.5f,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 10.0f
        );
        FloatBuffer viewBuf = BufferUtils.createFloatBuffer(16);
        view.get(viewBuf).rewind();
        glMultMatrixf(viewBuf);

        drawAxes();

        renderer.render(tetra, state);
    }

    private void drawAxes() {
        glBegin(GL_LINES);
        glColor3f(1,0,0); glVertex3f(0,0,0); glVertex3f(1,0,0);
        glColor3f(0,1,0); glVertex3f(0,0,0); glVertex3f(0,1,0);
        glColor3f(0,0,1); glVertex3f(0,0,0); glVertex3f(0,0,1);
        glEnd();
    }

    public static void main(String[] args) {
        new OpenGLIceberg().run();
    }
}
