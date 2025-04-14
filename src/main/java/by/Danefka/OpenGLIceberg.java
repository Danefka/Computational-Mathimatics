//package by.Danefka;
//
//import org.lwjgl.opengl.GL;
//
//import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
//import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL11.glEnd;
//import static org.lwjgl.system.MemoryUtil.NULL;
//
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL30.*;
//import org.lwjgl.glfw.GLFW;
//import org.lwjgl.system.MemoryStack;
//
//public class OpenGLIceberg {
//    private long window;
//
//    public void run() {
//        init();
//        loop();
//
//        // Освобождаем ресурсы
//        glfwFreeCallbacks(window);
//        glfwDestroyWindow(window);
//        glfwTerminate();
//    }
//
//    private void init() {
//        // Инициализация GLFW
//        if (!glfwInit()) throw new IllegalStateException("Не удалось инициализировать GLFW");
//
//        // Создание окна
//        window = glfwCreateWindow(800, 600, "OpenGL в Java", NULL, NULL);
//        if (window == NULL) throw new RuntimeException("Ошибка создания окна");
//
//        glfwMakeContextCurrent(window);
//        glfwShowWindow(window);
//    }
//
//    private void loop() {
//        // Создаём OpenGL-контекст
//        GL.createCapabilities();
//
//        // Устанавливаем область вывода
//        glViewport(0, 0, 800, 600);
//
//        // Настройка фона
//        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
//
//        while (!glfwWindowShouldClose(window)) {
//
//
//            // Очищаем экран
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//            // Рисуем треугольник
//            glBegin(GL_TRIANGLES);
//            glColor3f(1.0f, 0.0f, 0.0f); glVertex2f(-0.5f, -0.5f);
//            glColor3f(0.0f, 1.0f, 0.0f); glVertex2f( 0.5f, -0.5f);
//            glColor3f(0.0f, 0.0f, 1.0f); glVertex2f( 0.0f,  0.5f);
//            glEnd();
//
//            // Обновляем окно
//            glfwSwapBuffers(window);
//            glfwPollEvents();
//        }
//    }
//
//
//    public void draw(int screenWidth, int screenHeight, float[] state, float radius) {
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//        glViewport(0, 0, screenWidth, screenHeight);
//
//        float[] lightDir = {1.0f, 1.0f, 0.0f, 0.0f};
//        glLightfv(GL_LIGHT0, GL_POSITION, lightDir);
//
//        // Матрица проекции
//        glMatrixMode(GL_PROJECTION);
//        glLoadIdentity();
//        gluPerspective(45.0f, (float) screenWidth / screenHeight, 0.1f, 100.0f);
//
//        // Матрица модели/вида
//        glMatrixMode(GL_MODELVIEW);
//        glLoadIdentity();
//        gluLookAt(5.0f, 1.0f, 0.2f,  // Позиция камеры
//                0.0f, 0.0f, 0.0f,  // Точка, на которую смотрим
//                0.0f, 0.0f, 1.0f); // Вектор вверх
//
//        float[] color = {1.0f, 1.0f, Math.abs(state[1] / 3)};
//        float[] nocolor = {0, 0, 0, 1};
//        float[] ambcolor = {0.9f, 0.9f, 0.9f, 1};
//
//        glMaterialfv(GL_FRONT, GL_AMBIENT, ambcolor);
//        glMaterialfv(GL_FRONT, GL_DIFFUSE, color);
//        glMaterialfv(GL_FRONT, GL_EMISSION, nocolor);
//
//        glPushMatrix();
//        glTranslatef(0.0f, 0.0f, state[0]);
//        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
//
//        // Создание и отрисовка сферы
//        GLUquadric quadric = gluNewQuadric();
//        gluSphere(quadric, radius, 30, 30);
//        gluDeleteQuadric(quadric);
//
//        glPopMatrix();
//
//        drawAxes();
//
//        // Смена буфера
//        GLFW.glfwSwapBuffers(window);
//    }
//
//    private void drawAxes() {
//        glBegin(GL_LINES);
//
//        // Ось X (красный цвет)
//        glMaterialfv(GL_FRONT, GL_EMISSION, new float[]{1, 0, 0});
//        glVertex3f(0.0f, 0.0f, 0.0f);
//        glVertex3f(1.0f, 0.0f, 0.0f);
//
//        // Ось Y (зеленый цвет)
//        glMaterialfv(GL_FRONT, GL_EMISSION, new float[]{0, 1, 0});
//        glVertex3f(0.0f, 0.0f, 0.0f);
//        glVertex3f(0.0f, 1.0f, 0.0f);
//
//        // Ось Z (синий цвет)
//        glMaterialfv(GL_FRONT, GL_EMISSION, new float[]{0, 0, 1});
//        glVertex3f(0.0f, 0.0f, 0.0f);
//        glVertex3f(0.0f, 0.0f, 1.0f);
//
//        glEnd();
//    }
//
//
//    public static void main(String[] args) {
//        new OpenGLIceberg().run();
//    }
//}
