package utils;

public enum StatusEffect {
    NONE,
    BURN,       // FirePokemon → -5 HP per turn
    FREEZE,     // WaterPokemon → skip turn
    PARALYZE,   // ElectricPokemon → 30% gagal serang
    POISON      // GrassPokemon → -3 HP per turn
}