package tradedesk;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long window;

    private Shader shader;
    private Texture desk;
    private Texture monitor;

    private int vao;
    private int vbo;

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Nem sikerult elinditani a GLFW-t!");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(1280, 720, "TradeDesk 2D", NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Nem sikerult letrehozni az ablakot!");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        shader = new Shader();
        desk = new Texture("desk.png");
        monitor = new Texture("monitor.png");

        float[] vertices = {
                // x, y, texture_x, texture_y
                -1f, -1f, 0f, 0f,
                1f, -1f, 1f, 0f,
                1f,  1f, 1f, 1f,

                -1f, -1f, 0f, 0f,
                1f,  1f, 1f, 1f,
                -1f,  1f, 0f, 1f
        };

        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);

        shader.use();
        glUniform1i(glGetUniformLocation(glGetInteger(GL_CURRENT_PROGRAM), "image"), 0);

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

        shader.use();
        glBindVertexArray(vao);
        glActiveTexture(GL_TEXTURE0);


        draw(desk, 0.0f, -0.45f, 0.85f, 0.45f);


        draw(monitor, 0.0f, 0.12f, 0.55f, 0.48f);

        glBindVertexArray(0);

        glfwSwapBuffers(window);
    }

    private void draw(Texture texture, float x, float y, float width, float height) {
        texture.bind();

        float[] vertices = {
                x - width, y - height, 0f, 0f,
                x + width, y - height, 1f, 0f,
                x + width, y + height, 1f, 1f,

                x - width, y - height, 0f, 0f,
                x + width, y + height, 1f, 1f,
                x - width, y + height, 0f, 1f
        };

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void update() {
        glfwPollEvents();
    }

    public void cleanup() {
        if (desk != null) desk.cleanup();
        if (monitor != null) monitor.cleanup();
        if (shader != null) shader.cleanup();

        if (vbo != 0) glDeleteBuffers(vbo);
        if (vao != 0) glDeleteVertexArrays(vao);

        if (window != NULL) glfwDestroyWindow(window);

        glfwTerminate();

        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) callback.free();
    }
}