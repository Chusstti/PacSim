package PacSim.Graphics;

public class Animation implements Drawable {
    private static final double speed = 3.0f;
    private boolean isAnimated = false;
    private Texture texture;
    private double frameTimer = 0.0;
    private int frameX = 0;
    private int frameY = 0;
    private int limitX = 0;
    private int limitY = 0;

    private int srcX = 0;
    private int srcY = 0;

    public Animation(String file, int limitX, int limitY, int srcX, int srcY) {
        texture = Texture.loadTexture(file);
        this.limitX = limitX;
        this.limitY = limitY;
        this.srcX = srcX;
        this.srcY = srcY;
    }

    @Override
    public void draw(float x, float y, float z, double delta) {
        if (isAnimated) {
            frameTimer += 60.0 / 1000.0 * speed;
            if (frameTimer >= limitX) {
                frameTimer = 0.0f;
                frameX = 0;

                if (limitY > 0) {
                    frameY++;
                    if (frameY >= limitY) {
                        frameY = 0;
                        isAnimated = false;
                        return;
                    }
                }
            }

            frameX = (int)(frameTimer);
        }
        else {
            frameX = 0;
        }

        if (limitY > 0)
            z = frameY;

        texture.draw(x, y, frameX * srcX, z * srcY, (frameX + 1) * srcX, (z + 1) * srcY);
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }

    public boolean isAnimated() {
        return isAnimated;
    }
}