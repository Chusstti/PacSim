package PacSim.Game;

import PacSim.Graphics.Texture;
import PacSim.Graphics.Tile;
import PacSim.Graphics.TileMap;

import javax.swing.*;
import java.util.HashMap;

public class Game {
    public static TileMap tileMap;
    public static Player player;
    public static int finalPos;
    public static HashMap<String, Obj> objData = new HashMap<>();
    public static boolean finishGame = false;
    private Texture cartel;

    public Game() {
        MapLoader.loadMap("map1");
    }

    public void render(float x, float y, float z, double delta) {
        tileMap.draw(x, y, z, delta);
        player.draw(x, y, z, delta);

        if (cartel != null)
            cartel.draw(50, 128);
    }

    public Player getPlayer() {
        return player;
    }

    private boolean isLimit(int x, int y) {
        return (x < 0 || x >= (tileMap.getDimensionM()) || y < 0 || y >= (tileMap.getDimensionN()));
    }

    public void movePlayer(Movement movement) {
        if (finishGame)
            return;

        if (player.isMoving())
            return;

        player.setScrollDirectionX(0.0f);
        player.setScrollDirectionY(0.0f);

        int x = player.getPosX();
        int y = player.getPosY();

        switch (movement)
        {
            case NORTH:
                player.setOffsetAnimationY(1.0f);

                if (isLimit(x, y - 1))
                    return;

                if (tileMap.getTile(x, y - 1).isBlocked())
                    return;

                --y;
                player.setScrollDirectionY(-1.0f);
                break;

            case SOUTH:
                player.setOffsetAnimationY(0.0f);

                if (isLimit(x, y + 1))
                    return;

                if (tileMap.getTile(x, y + 1).isBlocked())
                    return;

                ++y;
                player.setScrollDirectionY(1.0f);
                break;

            case WEST:
                player.setOffsetAnimationY(2.0f);

                if (isLimit(x - 1, y))
                    return;

                if (tileMap.getTile(x - 1, y).isBlocked())
                    return;

                --x;
                player.setScrollDirectionX(-1.0f);
                break;

            case EAST:
                player.setOffsetAnimationY(3.0f);

                if (isLimit(x + 1, y))
                    return;

                if (tileMap.getTile(x + 1, y).isBlocked())
                    return;

                ++x;
                player.setScrollDirectionX(1.0f);
                break;
        }

        Tile tile = tileMap.getTile(x, y);
        player.setPosition(x, y);
        player.setMoving(true);
        player.setOffsetX(0.0f);
        player.setOffsetY(0.0f);

        finishGame = (player.getPosY() * tileMap.getDimensionM() + player.getPosX() == finalPos);
        if (finishGame) {
            cartel = new Texture("win.png");
        }

        if (!tile.isEmpty()) {
            for (Obj tempObj : tile.getObjects())
            {
                player.incrementShield(tempObj.getShieldRegenerated());
                player.incrementVitality(tempObj.getVitalityRegenerated());

                final int damage = -tempObj.getDamageInflicted();

                if (damage < 0) {
                    player.setEffect();

                    if (player.hasShield()) {
                        player.incrementShield(damage);
                    } else {
                        player.incrementVitality(damage);
                    }
                }
            }
            tile.setEmpty();
        }

        if (!finishGame && player.getVitality() <= 0) {
            finishGame = true;
            cartel = new Texture("lose.png");
        }
    }
}