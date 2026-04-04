package Models;

import utils.Type;

public class Pikachu extends ElectricPokemon {
    public Pikachu(String name, int hp, int attack, int defense) {
        super(name, hp, attack, defense, Type.ELECTRIC);
    }
}