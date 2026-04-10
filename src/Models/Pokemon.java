package Models;

import java.util.ArrayList;

import utils.Type;
import utils.StatusEffect;

public abstract class Pokemon {

    // ===== ATRIBUT =====
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected Type type;

    protected StatusEffect status = StatusEffect.NONE;
    protected ArrayList<Skill> skills = new ArrayList<>();
    protected int statusDuration = 0;

    // ===== CONSTRUCTOR =====
    public Pokemon(String name, int hp, int attack, int defense, Type type) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.type = type;
    }

    // ===== ABSTRACT ATTACK =====
    public abstract void attack(Pokemon enemy);

    // ===== TERIMA DAMAGE =====
    public void receiveDamage(int damage) {
        int finalDamage = Math.max(1, damage - defense);
        hp -= finalDamage;

        if (hp < 0)
            hp = 0;

        System.out.println(name + " menerima damage: " + finalDamage + " | HP sisa: " + hp);
    }

    // ===== CEK HIDUP =====
    public boolean isAlive() {
        return hp > 0;
    }

    // ===== SKILL =====
    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void useSkill(int index, Pokemon target) {
        if (index >= 0 && index < skills.size()) {
            skills.get(index).use(this, target);
        } else {
            System.out.println("Skill tidak valid!");
        }
    }

    // ===== STATUS SETTER =====
    public void setStatus(StatusEffect status) {
        if (this.status == StatusEffect.NONE) {
            this.status = status;
            this.statusDuration = 3;
            System.out.println(name + " terkena status: " + status);
        }

    }

    public StatusEffect getStatus() {
        return status;
    }

    public void clearStatus() {
        this.status = StatusEffect.NONE;
    }

    // ===== STATUS EFFECT LOGIC (PER TURN) =====
    public void applyStatusEffect() {

        if (status == StatusEffect.NONE)
            return;

        switch (status) {

            case BURN:
            case POISON:
                int damage = (int) (maxHp * status.getDamageRatio());
                hp -= damage;

                if (hp < 0)
                    hp = 0;

                System.out.println(name + " terkena " + status + " : -" + damage + " HP | Sisa HP: " + hp);
                break;

            case PARALYZE:
                System.out.println(name + " terkena PARALYZE ⚡");
                break;

            case FREEZE:
                System.out.println(name + " terkena FREEZE ❄️");

                if (Math.random() < 0.2) {
                    System.out.println(name + " berhasil lepas dari FREEZE!");
                    clearStatus();
                    return;
                }
                break;

            default:
                break;
        }

        // 🔥 KURANGIN DURASI
        statusDuration--;

        // 🔥 CEK HABIS
        if (statusDuration <= 0) {
            System.out.println(name + " sembuh dari " + status);
            clearStatus();
        }
    }

    // ===== CEK BISA GERAK ATAU TIDAK =====
    public boolean canMove() {

        switch (status) {

            case PARALYZE:
                double chance = Math.random();
                System.out.println(name + " roll PARALYZE: " + chance);

                if (chance < 0.5) {
                    System.out.println(name + " gagal bergerak karena PARALYZE! ⚡");
                    return false;
                }
                break;

            case FREEZE:
                System.out.println(name + " tidak bisa bergerak karena FREEZE! ❄️");
                return false;

            default:
                break;
        }

        return true;
    }

    public void showSkills() {
        System.out.println("Daftar Skill " + name + ":");

        for (int i = 0; i < skills.size(); i++) {
            System.out.println((i + 1) + ". " + skills.get(i).getName());
        }
    }

    // ===== HEAL =====
    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp)
            hp = maxHp;

        System.out.println(name + " di-heal +" + amount + " | HP sekarang: " + hp);
    }

    // ===== GETTER =====
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public Type getType() {
        return type;
    }

    // ===== SETTER =====
    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }
}