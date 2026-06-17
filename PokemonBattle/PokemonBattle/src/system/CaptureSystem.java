package system;

import Models.Pokemon;
import utils.StatusEffect;
import Models.LegendaryPokemon;

public class CaptureSystem {

    public static boolean tryCapture(Pokemon enemy) {

        // ❗ harus <= 20%
        if (enemy.getHpRatio() > 0.2) {
            System.out.println("Pokemon masih terlalu kuat untuk ditangkap!");
            return false;
        }

        double baseChance = enemy.isLegendary() ? 0.20 : 0.40;

        // bonus status
        if (enemy.getStatus() != StatusEffect.NONE) {
            baseChance += 0.05;
        }

        double roll = Math.random();

        System.out.println("Chance: " + baseChance);

        if (roll < baseChance) {
            System.out.println("🎉 Berhasil menangkap " + enemy.getName());
            return true;
        } else {
            System.out.println("❌ Gagal menangkap!");
            return false;
        }
    }
}