package tradedesk;

public class Main {

    public static void main(String[] args) {

        Window window = new Window();

        try {
            window.init();

            Input input = new Input(window.getHandle());
            input.init();

            while (!window.shouldClose()) {
                window.update();
                input.update();
                window.render();
            }

        } finally {
            window.cleanup();
        }
    }
}