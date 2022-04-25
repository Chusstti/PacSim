package PacSim.Game;

import PacSim.Graphics.Drawable;
import PacSim.Graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class Obj implements Drawable {
    private Texture texture;
    protected List<Obj> objects = new ArrayList<Obj>();

    public Obj(Texture texture) {
        this.texture = texture;
    }

    public Obj(String file) {
        this(Texture.loadTexture(file));
    }

    public Obj() {
        this("tile2.png");
    }

    public void add(Obj obj) {
        objects.add(obj);
    }

    public int getDamageInflicted() {
        return 0;
    }

    public int getVitalityRegenerated() {
        int total = 0;
        for (Obj obj : objects)
            total += obj.getVitalityRegenerated();

        return total;
    }

    public int getShieldRegenerated() {
        int total = 0;
        for (Obj obj : objects)
            total += obj.getShieldRegenerated();

        return total;
    }

    @Override
    public void draw(float x, float y, float z, double delta) {
        texture.draw(x + 8, y + 8, 16, 16);
    }
}
