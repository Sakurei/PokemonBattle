package Models;
import utils.Type;
import utils.TypeEffectiveness;

public class FlyingPokemon extends Pokemon {
    //constructor
    public FlyingPokemon(String name, int hp, int attack, int defense, Type type) {
        super(name, hp, attack, defense, type);
    }
    
    //Polymorphism
    @Override
    public void attack(Pokemon enemy){
        //multiplier type pokemon x2 advantage
        double multiplier = TypeEffectiveness.getMultiplier(this.type, enemy.getType());
        
        //hitung damage
        int baseDamage = this.attack - enemy.getDefense();
        int finalDamage = (int)(baseDamage * multiplier);

        if (finalDamage < 1) finalDamage = 1;

        System.out.println(this.name + " (Flying) menyerang " + enemy.getName());
        
        if (multiplier > 1) {
            System.out.println("🔥 Super Effective!");
        } else if (multiplier < 1) {
            System.out.println("😢 Not very effective...");
        }

        //kirim damage
        enemy.receiveDamage(finalDamage);
    }
}
