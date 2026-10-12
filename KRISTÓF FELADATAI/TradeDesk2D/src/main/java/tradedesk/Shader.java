package tradedesk;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final int program;

    public Shader() {
        String vertex_code = """
                #version 330 core

                layout (location = 0) in vec2 position;
                layout (location = 1) in vec2 texture_position;

                out vec2 texture_coords;

                void main() {
                    gl_Position = vec4(position, 0.0, 1.0);
                    texture_coords = texture_position;
                }
                """;

        String fragment_code = """
                #version 330 core

                in vec2 texture_coords;
                out vec4 color;

                uniform sampler2D image;

                void main() {
                    color = texture(image, texture_coords);
                }
                """;

        int vertex = compile(GL_VERTEX_SHADER, vertex_code);
        int fragment = compile(GL_FRAGMENT_SHADER, fragment_code);

        program = glCreateProgram();

        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetProgramInfoLog(program));
        }

        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    private int compile(int type, String code) {
        int shader = glCreateShader(type);
        glShaderSource(shader, code);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetShaderInfoLog(shader));
        }

        return shader;
    }

    public void use() {
        glUseProgram(program);
    }

    public void cleanup() {
        glDeleteProgram(program);
    }
}
