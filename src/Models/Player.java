package Models;

import java.util.ArrayList;

public class Player {

    private String name;
    private ArrayList<Pokemon> collection = new ArrayList<>();
    private Pokemon activePokemon;

    // ===== CONSTRUCTOR =====
    public Player(String name) {
        this.name = name;
    }

    // ===== TAMBAH POKEMON =====
    public void addPokemon(Pokemon pokemon) {
        collection.add(pokemon);
        System.out.println(pokemon.getName() + " masuk ke koleksi!");
    }

    // ===== TAMPILKAN KOLEKSI =====
    public void showCollection() {
        System.out.println("=== KOLEKSI POKEMON ===");

        for (int i = 0; i < collection.size(); i++) {
            Pokemon p = collection.get(i);

            if (p == activePokemon) {
                System.out.println((i + 1) + ". " + p.getName() + " ⭐");
            } else {
                System.out.println((i + 1) + ". " + p.getName());
            }
        }
    }

    // ===== PILIH POKEMON AKTIF =====
    public void setActivePokemon(int index) {
        if (index >= 0 && index < collection.size()) {
            activePokemon = collection.get(index);
            System.out.println("Kamu memilih " + activePokemon.getName() + " sebagai champion!");
        } else {
            System.out.println("Pilihan tidak valid!");
        }
    }

    // ===== GETTER =====
    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    public ArrayList<Pokemon> getCollection() {
        return collection;
    }
}