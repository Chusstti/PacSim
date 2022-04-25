package PacSim.Game;

public class Mine extends Obj {
    public Mine() {
        super("tile4.png");
    }

    @Override
    public int getDamageInflicted() {
        return 1;
    }

    @Override
    public int getVitalityRegenerated() {
        return 0;
    }

    @Override
    public int getShieldRegenerated() {
        return 0;
    }
    // honestamente no tiene sentido usar OOP, cuando son puros datos....
}
