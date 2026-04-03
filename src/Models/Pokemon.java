package Models;

import utils.Type;

public abstract class Pokemon {
    // Atribut yang digunakan
    protected String name;
    protected int hp;
    protected int attack;
    protected int defense;
    protected Type type;

    //constructor
    public Pokemon(String name, int hp, int attack, int defense, Type type){
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.type = type;
    }

    // Method abstract
    public abstract void attack(Pokemon enemy);

    //Demage yang diterima
    public void receiveDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " menerima damage: " + damage + " | HP sisa: " + hp);
    }

    //Pengeceakan apakah masih hidup
    public boolean isAlive() {
        return hp > 0;
    }

    //getter
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

    //setter
    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }
       
}
