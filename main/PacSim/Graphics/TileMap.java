package PacSim.Graphics;

import java.util.ArrayList;
import java.util.List;

public final class TileMap implements Drawable {
    private final int m;
    private final int n;
    private final List<Tile> tileMap;

    public TileMap(List<Tile> tileMap, int m, int n) {
        this.m = m;
        this.n = n;

        this.tileMap = tileMap;
    }

    public TileMap(int m, int n) {
        this.m = m;
        this.n = n;

        tileMap = new ArrayList<>(m * n);
        for (int i = 0; i < m * n; ++i) {
            tileMap.add(i, new Tile());
        }
    }

    @Override
    public void draw(float x, float y, float z, double delta) {
        int counter = 0;
        for (Tile tile : tileMap) {
            final int posX = counter % m;
            final int posY = counter / m;

            tile.draw(x + (float)(posX * 32), y + (float)(posY * 32), z, delta);
            counter++;
        }
    }

    public Tile getTile(int index) {
        return tileMap.get(index);
    }

    public Tile getTile(int x, int y) {
        return tileMap.get(y * m + x);
    }

    public int size() {
        return tileMap.size();
    }

    public int getDimensionM() {
        return m;
    }

    public int getDimensionN() {
        return n;
    }
}
