package Models;

import utils.Type;
import utils.TypeEffectiveness;

public class BasicPokemon extends Pokemon {

    public BasicPokemon(String name, int hp, int attack, int defense, Type type) {
        super(name, hp, attack, defense, type);
    }

    @Override
    public void attack(Pokemon enemy){
        double multiplier = TypeEffectiveness.getMultiplier(this.type, enemy.getType());

        int baseDamage = this.attack;
        int finalDamage = (int)(baseDamage * multiplier);

        if (finalDamage < 1) finalDamage = 1;

        System.out.println(name + " menyerang " + enemy.getName());

        if (multiplier > 1) {
            System.out.println("🔥 Super Effective!");
        } else if (multiplier < 1) {
            System.out.println("😢 Not very effective...");
        }

        enemy.receiveDamage(finalDamage);
    }
}