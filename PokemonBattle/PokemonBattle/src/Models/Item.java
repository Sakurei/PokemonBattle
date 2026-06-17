package Models;

import utils.StatusEffect;

public class Item {

    private String name;
    private int healAmount;
    private boolean cureStatus;
    private double successChance;

    // ===== CONSTRUCTOR =====
    public Item(String name, int healAmount, boolean cureStatus, double successChance) {
        this.name = name;
        this.healAmount = healAmount;
        this.cureStatus = cureStatus;
        this.successChance = successChance;
    }

    // ===== USE ITEM =====
    public void use(Pokemon target) {
        System.out.println(target.getName() + " menggunakan " + name);

        // ===== HEAL =====
        if (healAmount > 0) {
            target.heal(healAmount);
        }

        // ===== CURE STATUS =====
        if (cureStatus) {

            // kalau tidak ada status
            if (target.getStatus() == StatusEffect.NONE) {
                System.out.println("Tidak ada status yang perlu disembuhkan.");
                return;
            }

            double roll = Math.random();

            if (roll < successChance) {
                System.out.println(name + " berhasil menyembuhkan " + target.getStatus());
                target.clearStatus();
            } else {
                System.out.println(name + " gagal menyembuhkan status!");
            }
        }
    }

    // ===== GETTER =====
    public String getName() {
        return name;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public boolean isCureStatus() {
        return cureStatus;
    }

    public double getSuccessChance() {
        return successChance;
    }
}