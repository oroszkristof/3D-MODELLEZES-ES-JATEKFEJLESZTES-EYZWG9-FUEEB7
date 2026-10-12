package tradedesk;

import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Texture {

    private final int id;

    public Texture(String file) {
        try (var stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);

            ByteBuffer image = STBImage.stbi_load(
                    "src/main/resources/" + file,
                    width, height, channels, 4
            );

            if (image == null) {
                throw new RuntimeException(
                        "Nem sikerult betolteni: " + file
                                + " - " + STBImage.stbi_failure_reason()
                );
            }

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR
            );

            glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MAG_FILTER,
                    GL_LINEAR
            );

            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    width.get(0),
                    height.get(0),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    image
            );

            STBImage.stbi_image_free(image);
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}