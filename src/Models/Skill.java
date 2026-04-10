package Models;

import utils.Type;
import utils.TypeEffectiveness;
import utils.StatusEffect;

public class Skill {

    private String name;
    private Type type;
    private int power;
    private StatusEffect effect;
    private double effectChance; // contoh: 0.3 = 30%

    // ===== CONSTRUCTOR =====
    public Skill(String name, Type type, int power, StatusEffect effect, double effectChance) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.effect = effect;
        this.effectChance = effectChance;
    }

    // ===== USE SKILL =====
    public void use(Pokemon user, Pokemon target) {

        System.out.println(user.getName() + " menggunakan " + name);

        // ===== HITUNG MULTIPLIER =====
        double multiplier = TypeEffectiveness.getMultiplier(this.type, target.getType());

        // ===== HITUNG DAMAGE =====
        int baseDamage = this.power + user.getAttack();
        int finalDamage = (int)(baseDamage * multiplier);

        if (finalDamage < 1) finalDamage = 1;

        // ===== INFO EFEKTIVITAS =====
        if (multiplier > 1) {
            System.out.println("🔥 Super Effective!");
        } else if (multiplier < 1) {
            System.out.println("😢 Not very effective...");
        }

        // ===== APPLY DAMAGE =====
        target.receiveDamage(finalDamage);

        // ===== STATUS EFFECT (PAKAI CHANCE) =====
        if (effect != StatusEffect.NONE) {
            double random = Math.random(); // 0.0 - 1.0

            if (random < effectChance) {
                target.setStatus(effect);
            }
        }
    }

    // ===== GETTER =====
    public String getName() { return name; }
    public Type getType() { return type; }
    public int getPower() { return power; }
    public StatusEffect getEffect() { return effect; }
    public double getEffectChance() { return effectChance; }
}