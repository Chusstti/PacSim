package PacSim.Game;

import PacSim.Graphics.Animation;
import PacSim.Graphics.Drawable;
import PacSim.Graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class Player implements Drawable {
    private Animation animation;
    private List<Animation> effects;
    private Texture textureLife;
    private Texture textureShield;
    private int vitality = 3;
    private int shield = 1;
    private int x, y;
    private float offsetX = 0.0f, offsetY = 0.0f;
    private float scrollDirectionX = 0.0f, scrollDirectionY = 0.0f;
    private float offsetAnimationY = 0.0f;
    private boolean isMoving = false;

    public Player(int iPos) {
        this(iPos % Game.tileMap.getDimensionM(), iPos / Game.tileMap.getDimensionM());
    }

    public Player(int x, int y) {
        animation = new Animation("222.png", 5, 0, 25, 58);

        effects = new ArrayList<Animation>();
        effects.add(new Animation("explosion.png", 4, 4, 126, 128));
        effects.add(new Animation("explosion.png", 4, 4, 126, 128));
        effects.add(new Animation("explosion.png", 4, 4, 126, 128));
        effects.add(new Animation("explosion.png", 4, 4, 126, 128));
        effects.add(new Animation("explosion.png", 4, 4, 126, 128));

        textureLife = new Texture("potion.png");
        textureShield = new Texture("shield.png");
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(float x, float y, float z, double delta) {
        int posX = (this.x) * 32;
        int posY = (this.y) * 32;

        if (isMoving) {
            if (offsetX * scrollDirectionX >= 32.0f) {
                offsetX = 0.0f;
                scrollDirectionX = 0.0f;
                isMoving = false;
                animation.setAnimated(false);
            }

            if (offsetY * scrollDirectionY >= 32.0f) {
                offsetY = 0.0f;
                scrollDirectionY = 0.0f;
                isMoving = false;
                animation.setAnimated(false);
            }

            offsetX += scrollDirectionX;
            offsetY += scrollDirectionY;

            if (scrollDirectionX != 0.0f)
                posX -= 32 * scrollDirectionX;

            if (scrollDirectionY != 0.0f)
                posY -= 32 * scrollDirectionY;
        }

        final float totalX = x + posX + offsetX;
        final float totalY = y + posY + offsetY;

        animation.draw(totalX, totalY - 32, offsetAnimationY, delta);

        for (Animation effect : effects) {
            if (effect.isAnimated())
                effect.draw(totalX - 50, totalY - 50, 0, delta);
        }

        for (int i = 0; i < vitality; ++i)
            textureLife.draw(8 + i * 12, 8, 32/2, 42/2);

        for (int i = 0; i < shield; ++i)
            textureShield.draw(6 + i * 24, 8*5, 53/2, 63/2);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
        animation.setAnimated(isMoving);
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public void setScrollDirectionX(float scrollDirectionX) {
        this.scrollDirectionX = scrollDirectionX;
    }

    public void setScrollDirectionY(float scrollDirectionY) {
        this.scrollDirectionY = scrollDirectionY;
    }

    public int getPosX() {
        return x;
    }

    public int getPosY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setOffsetAnimationY(float offsetAnimationY) {
        this.offsetAnimationY = offsetAnimationY;
    }

    public boolean isAlive() {
        return vitality > 0;
    }

    public int getVitality() {
        return vitality;
    }

    public int getShield() {
        return shield;
    }

    public void incrementShield(int value) {
        shield += value;
    }

    public void incrementVitality(int value) {
        vitality += value;
    }

    public boolean hasShield() {
        return shield > 0;
    }

    public void setEffect() {
        for (Animation effect : effects) {
            if (!effect.isAnimated()) {
                effect.setAnimated(true);
                return;
            }
        }
    }
}
