package Models;

import utils.StatusEffect;

public class Skill {
    private String skillName;
    private int damage;
    private StatusEffect statusEffect;
    private double effectChance;

    public Skill(String skillName, int damage, StatusEffect statusEffect, double effectChance) {
        this.skillName = skillName;
        this.damage = damage;
        this.statusEffect = statusEffect;
        this.effectChance = effectChance;
    }

    public void use(Pokemon user, Pokemon target) {
        System.out.println(user.getName() + " menggunakan " + skillName + "!");

        int finalDamage = damage - target.getDefense();
        if (finalDamage < 1) finalDamage = 1;

        target.receiveDamage(finalDamage);

        if (statusEffect != StatusEffect.NONE) {
            double roll = Math.random();
            if (roll < effectChance) {
                target.setStatus(statusEffect);
                System.out.println(target.getName() + " terkena efek " + statusEffect + "!");
            }
        }
    }

    public String getSkillName() {
        return skillName;
    }

    public int getDamage() {
        return damage;
    }
}
