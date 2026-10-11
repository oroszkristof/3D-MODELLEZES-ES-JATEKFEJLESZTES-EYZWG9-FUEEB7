package tradedesk;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private final long window;

    private double mouse_x;
    private double mouse_y;
    private double scroll_y;

    private boolean left_pressed;
    private boolean right_pressed;
    private boolean dragging;

    private double press_x;
    private double press_y;
    private double last_click_time = -1;

    public Input(long window) {
        this.window = window;
    }

    public void init() {

        glfwSetCursorPosCallback(window, (win, x, y) -> {
            mouse_x = x;
            mouse_y = y;

            if (left_pressed) {
                double dx = mouse_x - press_x;
                double dy = mouse_y - press_y;

                if (!dragging && dx * dx + dy * dy > 25) {
                    dragging = true;
                    System.out.println("Huzas kezdete");
                }
            }
        });

        glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {

            if (button == GLFW_MOUSE_BUTTON_LEFT) {

                if (action == GLFW_PRESS) {
                    left_pressed = true;
                    dragging = false;

                    press_x = mouse_x;
                    press_y = mouse_y;

                    double now = glfwGetTime();

                    if (last_click_time >= 0 &&
                            now - last_click_time < 0.3) {
                        System.out.println("Dupla kattintas");
                    } else {
                        System.out.println("Bal kattintas");
                    }

                    last_click_time = now;
                }

                if (action == GLFW_RELEASE) {
                    left_pressed = false;

                    if (dragging) {
                        System.out.println("Huzas vege");
                    }

                    dragging = false;
                }
            }

            if (button == GLFW_MOUSE_BUTTON_RIGHT) {

                if (action == GLFW_PRESS) {
                    right_pressed = true;
                    System.out.println("Jobb kattintas");
                }

                if (action == GLFW_RELEASE) {
                    right_pressed = false;
                }
            }
        });

        glfwSetScrollCallback(window, (win, x_offset, y_offset) -> {
            scroll_y += y_offset;

            if (y_offset > 0) {
                System.out.println("Gorgetes fel");
            } else if (y_offset < 0) {
                System.out.println("Gorgetes le");
            }
        });
    }

    public void update() {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }
    }

    public double getMouseX() {
        return mouse_x;
    }

    public double getMouseY() {
        return mouse_y;
    }

    public double getScrollY() {
        return scroll_y;
    }

    public boolean isLeftPressed() {
        return left_pressed;
    }

    public boolean isRightPressed() {
        return right_pressed;
    }

    public boolean isDragging() {
        return dragging;
    }
}