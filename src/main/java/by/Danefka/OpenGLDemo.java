package by.Danefka;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class OpenGLDemo {
    private long window;

    public void run() {
        init();
        loop();

        // Освобождаем ресурсы
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void init() {
        // Инициализация GLFW
        if (!glfwInit()) throw new IllegalStateException("Не удалось инициализировать GLFW");

        // Создание окна
        window = glfwCreateWindow(800, 600, "OpenGL в Java", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Ошибка создания окна");

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
    }

    private void loop() {
        // Создаём OpenGL-контекст
        GL.createCapabilities();

        // Устанавливаем область вывода
        glViewport(0, 0, 800, 600);

        // Настройка фона
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

        while (!glfwWindowShouldClose(window)) {
            // Очищаем экран
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Рисуем треугольник
            glBegin(GL_TRIANGLES);
            glColor3f(1.0f, 0.0f, 0.0f); glVertex2f(-0.5f, -0.5f);
            glColor3f(0.0f, 1.0f, 0.0f); glVertex2f( 0.5f, -0.5f);
            glColor3f(0.0f, 0.0f, 1.0f); glVertex2f( 0.0f,  0.5f);
            glEnd();

            // Обновляем окно
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new OpenGLDemo().run();
    }
}
