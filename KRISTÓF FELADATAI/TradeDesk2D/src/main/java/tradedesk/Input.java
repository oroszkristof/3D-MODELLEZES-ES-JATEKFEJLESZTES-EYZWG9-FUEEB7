package tradedesk;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private final long window;

    public Input(long window) {
        this.window = window;
    }

    public void init() {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public void update() {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }
    }
}