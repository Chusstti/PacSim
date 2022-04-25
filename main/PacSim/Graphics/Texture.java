package PacSim.Graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.util.HashMap;

public class Texture {
    public static HashMap<String, Texture> textureMap = new HashMap<>();

    public static Texture loadTexture(String file) {
        if (!textureMap.containsKey(file))
            textureMap.put(file, new Texture(file));

        return textureMap.get(file);
    }

    private int textureID;
    private int width, height;

    public Texture(String file) {
        createTextureFromFile(file);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void createTextureFromFile(String file) {
        final Image image = Image.loadImage("textures/" + file);
        width = image.getWidth();
        height = image.getHeight();
        textureID = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image.getBufferImage());
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        STBImage.stbi_image_free(image.getBufferImage());
    }

    public void draw(float x, float y, float x1, float y1, float x2, float y2) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        {
            final float sizeW = (x2 - x1);// + 0.001f;
            final float sizeH = (y2 - y1);// + 0.001f;

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(x1/width, y1/height);
            GL11.glVertex2f(x, y);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(x2/width, y1/height);
            GL11.glVertex2f(x + sizeW, y);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(x1/width, y2/height);
            GL11.glVertex2f(x, y + sizeH);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(x2/width, y2/height);
            GL11.glVertex2f(x + sizeW, y + sizeH);
        }
        GL11.glEnd();
    }

    //Es para no darle importancia al tamaño de la textura.
    public void draw(float x1, float y1, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex2f(x1, y1);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex2f(x1 + (float)width, y1);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex2f(x1, y1 + (float)height);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex2f(x1 + (float)width, y1 + (float)height);
        }
        GL11.glEnd();
    }

    public void draw(float x, float y) {
        draw(x, y, width, height);
    }
}
