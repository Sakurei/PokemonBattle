package Models;

import utils.StatusEffect;

public class StatusEffectHandler {
    public static boolean processTurn(Pokemon pokemon) {
        StatusEffect status = pokemon.getStatus();

        if (status == StatusEffect.BURN) {
            System.out.println(pokemon.getName() + " terbakar! -5 HP");
            pokemon.receiveDamage(5);
            return true;
        }

        if (status == StatusEffect.POISON) {
            System.out.println(pokemon.getName() + " keracunan! -3 HP");
            pokemon.receiveDamage(3);
            return true;
        }

        if (status == StatusEffect.FREEZE) {
            System.out.println(pokemon.getName() + " membeku! Tidak bisa menyerang!");
            return false;
        }

        if (status == StatusEffect.PARALYZE) {
            System.out.println(pokemon.getName() + " lumpuh!");
            double roll = Math.random();
            if (roll < 0.3) {
                System.out.println(pokemon.getName() + " gagal menyerang!");
                return false;
            }
            return true;
        }

        return true;
    }
}