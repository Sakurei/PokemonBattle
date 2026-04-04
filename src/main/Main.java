package main;

import Models.Pokemon;
import Models.Pikachu;
import Models.Charmander;
import Models.HealItem;

public class Main {
    public static void main(String[] args) {
        // Membuat Pokemon
        Pokemon pikachu = new Pikachu("Pikachu", 100, 20, 10);
        Pokemon charmander = new Charmander("Charmander", 120, 25, 15);

        // Membuat heal item
        HealItem potion = new HealItem("Potion", 30);

        System.out.println("=== TEST HEAL ITEM ===");
        System.out.println(pikachu.getName() + " HP awal: " + pikachu.getHp());
        System.out.println(charmander.getName() + " HP awal: " + charmander.getHp());

        // Pikachu menyerang Charmander
        System.out.println("\n--- Pikachu menyerang Charmander ---");
        pikachu.attack(charmander);

        System.out.println("\n--- Status setelah serangan ---");
        System.out.println(pikachu.getName() + " HP: " + pikachu.getHp());
        System.out.println(charmander.getName() + " HP: " + charmander.getHp());

        // Charmander menyerang Pikachu
        System.out.println("\n--- Charmander menyerang Pikachu ---");
        charmander.attack(pikachu);

        System.out.println("\n--- Status setelah serangan ---");
        System.out.println(pikachu.getName() + " HP: " + pikachu.getHp());
        System.out.println(charmander.getName() + " HP: " + charmander.getHp());

        // Menggunakan heal item pada Pikachu
        System.out.println("\n--- Menggunakan Potion pada Pikachu ---");
        potion.use(pikachu);

        System.out.println("\n--- Status akhir ---");
        System.out.println(pikachu.getName() + " HP: " + pikachu.getHp());
        System.out.println(charmander.getName() + " HP: " + charmander.getHp());
    }
}
