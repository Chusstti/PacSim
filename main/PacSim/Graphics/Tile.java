package PacSim.Graphics;

import PacSim.Game.Obj;
import java.util.ArrayList;
import java.util.List;

public class Tile implements Drawable {
    public final static int TILE_SIZE = 32;
    private Texture texture;
    private boolean hasBlock = false;

    private List<Obj> listObjects = new ArrayList<>();

    public Tile(String file, boolean isBlock) {
        texture = Texture.loadTexture(file);
        setBlocked(isBlock);
    }

    public Tile(boolean isBlock) {
        this("tile1.png", isBlock);
    }

    public Tile() {
        this(false);
    }

    @Override
    public void draw(float x, float y, float z, double delta) {
        texture.draw(x, y, TILE_SIZE, TILE_SIZE);

        if (!isEmpty()) {
            for (Obj obj : listObjects)
                obj.draw(x, y, z, delta);
        }
    }

    public boolean isBlocked() {
        return hasBlock;
    }

    public void setBlocked(boolean blocked) {
        this.hasBlock = blocked;
        if (blocked)
            texture = Texture.loadTexture("tile3.png");
    }

    public boolean isEmpty() {
        return (listObjects.size() <= 0);
    }

    public List<Obj> getObjects() {
        if (isEmpty())
            return null;

        return listObjects;
    }

    public void setObject(Obj obj) {
        this.listObjects.add(obj);
    }

    public void setEmpty() {
        listObjects.clear();
    }
}
