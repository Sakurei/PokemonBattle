package Models;

import utils.Type;
import utils.TypeEffectiveness;
import utils.StatusEffect;

public class LegendaryPokemon extends Pokemon {

    public LegendaryPokemon(String name, int hp, int attack, int defense, Type type) {
        super(name, hp, attack, defense, type);

        // 🔥 tandai sebagai legendary
        this.isLegendary = true;
    }

    @Override
    public void attack(Pokemon enemy){
        double multiplier = TypeEffectiveness.getMultiplier(this.type, enemy.getType());

        // Legendary bonus damage
        int baseDamage = (int)(this.attack * 1.5);
        int finalDamage = (int)(baseDamage * multiplier);

        if (finalDamage < 1) finalDamage = 1;

        System.out.println("🌟 Legendary Power Activated!");
        System.out.println(name + " menyerang " + enemy.getName());

        enemy.receiveDamage(finalDamage);

        // Bonus effect: 30% chance kasih status
        double chance = Math.random();

        if (chance < 0.3) {
            enemy.setStatus(StatusEffect.BURN);
            System.out.println("🔥 " + enemy.getName() + " terkena BURN!");
        }
    }
}