package main;

import java.util.Scanner;
import Models.*;
import battles.BattleSystem;
import utils.Type;
import data.SkillData;
import java.util.ArrayList;
import java.util.Random;
import system.*;

public class Game {

    private Player player;
    private Scanner input = new Scanner(System.in);

    public Game() {
        player = new Player("Ash");

        // 🔥 starter
        Pokemon starter = new BasicPokemon("Pikachu", 100, 20, 5, Type.ELECTRIC);
        assignRandomSkills(starter); // 🔥 WAJIB
        player.addPokemon(starter);

        choosePokemon();
    }

    // ===== PILIH POKEMON =====
    public void choosePokemon() {
        player.showCollection();

        System.out.print("Pilih Pokemon: ");
        int choice = input.nextInt();

        player.setActivePokemon(choice - 1);
    }

    // ===== RANDOM ENEMY =====
    public Pokemon getRandomEnemy() {

        double roll = Math.random();

        if (roll < 0.2) {
            return new LegendaryPokemon("Bulbasor", 150, 30, 10, Type.GRASS);
        } else {
            return new BasicPokemon("Charmander", 100, 18, 5, Type.FIRE);
        }
    }

    public void assignRandomSkills(Pokemon pokemon) {

        ArrayList<Skill> skills = SkillData.getSkillsByType(pokemon.getType());
        Random rand = new Random();

        while (pokemon.getSkills().size() < 2) {
            Skill s = skills.get(rand.nextInt(skills.size()));

            if (!pokemon.getSkills().contains(s)) {
                pokemon.addSkill(s);
            }
        }
    }

    // ===== MAIN LOOP =====
    public void start() {

        boolean running = true;

        while (running) {

            System.out.println("\n=== MENU ===");
            System.out.println("1. Battle");
            System.out.println("2. Lihat Koleksi");
            System.out.println("3. Ganti Pokemon");
            System.out.println("4. Exit");
            System.out.print("Pilih: ");

            int choice = input.nextInt();

            switch (choice) {

                case 1:
                    Pokemon enemy = getRandomEnemy();
                    assignRandomSkills(enemy); // 🔥 WAJIB

                    System.out.println("Lawan muncul: " + enemy.getName());

                    BattleSystem battle = new BattleSystem(player, enemy);
                    battle.startBattle();
                    break;

                case 2:
                    player.showCollection();
                    break;

                case 3:
                    choosePokemon();
                    break;

                case 4:
                    system.SaveSystem.savePlayer(player);
                    running = false;
                    break;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}