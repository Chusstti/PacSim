package PacSim.Graphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import PacSim.Game.MapLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Image {
    private ByteBuffer byteBuffer;
    private int width, heigh;

    Image(int width, int heigh, ByteBuffer image) {
        this.byteBuffer = image;
        this.heigh = heigh;
        this.width = width;
    }

    public static Image loadImage(String file) {
        int width, heigh;

        ByteBuffer imageBuffer;
        ByteBuffer imageTemp;

        try {
            imageBuffer = ioResourceToByteBuffer(file, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer channels = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            if (!STBImage.stbi_info_from_memory(imageBuffer, w, h, channels)) {
                throw new RuntimeException("Failed to read image information: " + STBImage.stbi_failure_reason());
            }

            imageTemp = STBImage.stbi_load_from_memory(imageBuffer, w, h, channels, STBImage.STBI_rgb_alpha); //RGBA

            width = w.get();
            heigh = h.get();
        }

        return new Image(width, heigh, imageTemp);
    }

    public ByteBuffer getBufferImage() {
        return byteBuffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return heigh;
    }


    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    private static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
                while (fc.read(buffer) != -1) {
                    ;
                }
            }
        } else {
            try (
                    InputStream source = Image.class.getClassLoader().getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)
            ) {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }
}