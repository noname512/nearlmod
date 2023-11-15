package nearlmod.util;

public class CostReserves {
    private static int curReserves = 0;
    public static void addReserves(int amount) {
        curReserves += amount;
        if (amount > 0) {
            CostEnergyOrb.secondVfxTimer = 2.0F;
        }
    }

    public static int reserveCount() {
        return curReserves;
    }

    public static void resetReserves() {
        curReserves = 0;
    }
}
