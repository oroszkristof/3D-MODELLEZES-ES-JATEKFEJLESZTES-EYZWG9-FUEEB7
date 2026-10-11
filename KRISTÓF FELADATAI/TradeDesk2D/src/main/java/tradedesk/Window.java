package tradedesk;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long window;

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Nem sikerült inicializálni a GLFW-t!");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(
                1280, 720,
                "TradeDesk 2D",
                NULL, NULL
        );

        if (window == NULL) {
            throw new RuntimeException("Nem sikerült létrehozni a játékablakot!");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        GL.createCapabilities();

        glfwShowWindow(window);
    }

    public long getHandle() {
        return window;
    }

    public void render() {
        int[] width = new int[1];
        int[] height = new int[1];

        glfwGetFramebufferSize(window, width, height);
        glViewport(0, 0, width[0], height[0]);

        glClearColor(0.08f, 0.10f, 0.15f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void update() {
        glfwPollEvents();
    }

    public void cleanup() {
        glfwDestroyWindow(window);
        glfwTerminate();

        GLFWErrorCallback callback = glfwSetErrorCallback(null);

        if (callback != null) {
            callback.free();
        }
    }
}