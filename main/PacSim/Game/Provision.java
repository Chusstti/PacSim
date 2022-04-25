package PacSim.Game;

import java.util.ArrayList;
import java.util.List;

public class Provision extends Obj {
    private int shieldPoints = 0;
    private int vitalityPoints = 0;

    public Provision(int shieldPoints, int vitalityPoints) {
        super("tile2.png");
        this.shieldPoints = shieldPoints;
        this.vitalityPoints = vitalityPoints;
    }

    @Override
    public int getDamageInflicted() {
        return 0;
    }

    @Override
    public int getVitalityRegenerated() {
        return vitalityPoints;
    }

    @Override
    public int getShieldRegenerated() {
        return shieldPoints;
    }
}
