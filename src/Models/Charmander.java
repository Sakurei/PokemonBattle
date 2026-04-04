package Models;

import utils.Type;

public class Charmander extends FirePokemon {
    public Charmander(String name, int hp, int attack, int defense) {
        super(name, hp, attack, defense, Type.FIRE);
    }
}