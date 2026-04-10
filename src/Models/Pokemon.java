package Models;

import java.util.ArrayList;
import utils.Type;
import utils.StatusEffect;

public abstract class Pokemon {

    protected String name;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected Type type;

    protected boolean isLegendary = false;

    protected StatusEffect status = StatusEffect.NONE;
    protected int statusDuration = 0;

    protected ArrayList<Skill> skills = new ArrayList<>();

    public Pokemon(String name, int hp, int attack, int defense, Type type) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.type = type;
    }

    public abstract void attack(Pokemon enemy);

    public void receiveDamage(int damage) {
        int finalDamage = Math.max(1, damage - defense);
        hp -= finalDamage;
        if (hp < 0)
            hp = 0;

        System.out.println(name + " menerima damage: " + finalDamage + " | HP sisa: " + hp);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void useSkill(int index, Pokemon target) {
        skills.get(index).use(this, target);
    }

    public void heal(int amount) {
        hp += amount;

        if (hp > maxHp) {
            hp = maxHp;
        }

        System.out.println(name + " di-heal +" + amount + " | HP sekarang: " + hp);
    }

    // 🔥 SHOW SKILL + DAMAGE PREVIEW
    public void showSkills(Pokemon target) {
        System.out.println("Daftar Skill " + name + ":");

        for (int i = 0; i < skills.size(); i++) {
            Skill s = skills.get(i);

            int estimated = s.estimateDamage(this, target);

            System.out.println(
                    (i + 1) + ". " +
                            s.getName() +
                            " (Power: " + s.getPower() +
                            " | Est: " + estimated + ")");
        }
    }

    public void setStatus(StatusEffect status) {
        if (this.status == StatusEffect.NONE) {
            this.status = status;
            this.statusDuration = 3;
            System.out.println(name + " terkena status: " + status);
        }
    }

    public void clearStatus() {
        status = StatusEffect.NONE;
        statusDuration = 0;
    }

    public StatusEffect getStatus() {
        return status;
    }

    public void applyStatusEffect() {

        if (status == StatusEffect.NONE)
            return;

        switch (status) {

            case BURN:
            case POISON:
                int damage = (int) (hp * status.getDamageRatio());
                if (damage < 1)
                    damage = 1;

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
                    System.out.println(name + " berhasil lepas!");
                    clearStatus();
                    return;
                }
                break;

            default:
                break;
        }

        statusDuration--;

        if (statusDuration <= 0) {
            System.out.println(name + " sembuh dari " + status);
            clearStatus();
        }
    }

    public boolean canMove() {
        if (status == StatusEffect.PARALYZE) {
            double roll = Math.random();
            System.out.println(name + " roll PARALYZE: " + roll);

            if (roll < 0.5) {
                System.out.println(name + " gagal bergerak!");
                return false;
            }
        }

        if (status == StatusEffect.FREEZE) {
            System.out.println(name + " tidak bisa bergerak!");
            return false;
        }

        return true;
    }

    public double getHpRatio() {
        return (double) hp / maxHp;
    }

    public boolean isLegendary() {
        return isLegendary;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public Type getType() {
        return type;
    }
}