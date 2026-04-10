package utils;

public enum StatusEffect { 
    NONE(0),

    BURN(0.1),     // 10% HP
    POISON(0.125), // 12.5% HP
    PARALYZE(0),
    FREEZE(0);

    private double damageRatio;

    StatusEffect(double damageRatio) {
        this.damageRatio = damageRatio;
    }

    public double getDamageRatio() {
        return damageRatio;
    }
}