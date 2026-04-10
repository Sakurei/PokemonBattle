package battles;

import java.util.Scanner;
import Models.Pokemon;
import Models.Item;
import java.util.ArrayList;

public class BattleSystem {

    private Pokemon player;
    private Pokemon enemy;
    private Scanner input = new Scanner(System.in);

    private ArrayList<Item> inventory = new ArrayList<>();

    public BattleSystem(Pokemon player, Pokemon enemy) {
        this.player = player;
        this.enemy = enemy;

        // 🔥 ISI INVENTORY (DUMMY)
        inventory.add(new Item("Potion", 30, false, 1.0));
        inventory.add(new Item("Antidote", 0, true, 0.7));
    }

    public void startBattle() {
        System.out.println("=== BATTLE START ===");

        int turn = 1;

        while (player.isAlive() && enemy.isAlive()) {

            System.out.println("\n=== TURN " + turn + " ===");

            // ===== PLAYER TURN =====
            System.out.println("\nGiliran Player:");

            player.applyStatusEffect();
            if (!player.isAlive()) break;

            if (player.canMove()) {

                // 🔥 MENU UTAMA
                System.out.println("1. Attack");
                System.out.println("2. Use Item");
                System.out.print("Pilih aksi: ");
                int action = input.nextInt();

                // ===== ATTACK =====
                if (action == 1) {

                    player.showSkills();

                    System.out.print("Pilih skill: ");
                    int choice = input.nextInt();

                    if (choice < 1 || choice > player.getSkills().size()) {
                        System.out.println("Pilihan tidak valid! Pakai skill pertama.");
                        choice = 1;
                    }

                    player.useSkill(choice - 1, enemy);
                }

                // ===== USE ITEM =====
                else if (action == 2) {

                    if (inventory.isEmpty()) {
                        System.out.println("Inventory kosong!");
                    } else {

                        System.out.println("Daftar Item:");
                        for (int i = 0; i < inventory.size(); i++) {
                            System.out.println((i + 1) + ". " + inventory.get(i).getName());
                        }

                        System.out.print("Pilih item: ");
                        int itemChoice = input.nextInt();

                        if (itemChoice >= 1 && itemChoice <= inventory.size()) {
                            inventory.get(itemChoice - 1).use(player);
                        } else {
                            System.out.println("Pilihan item tidak valid!");
                        }
                    }
                }

                else {
                    System.out.println("Pilihan tidak valid! Skip turn.");
                }
            }

            if (!enemy.isAlive()) break;

            // ===== ENEMY TURN =====
            System.out.println("\nGiliran Enemy:");

            enemy.applyStatusEffect();
            if (!enemy.isAlive()) break;

            if (enemy.canMove()) {
                int randomSkill = (int)(Math.random() * enemy.getSkills().size());
                enemy.useSkill(randomSkill, player);
            }

            if (!player.isAlive()) break;

            turn++;
        }

        // ===== HASIL =====
        System.out.println("\n=== BATTLE END ===");

        if (player.isAlive()) {
            System.out.println("Player Menang!");
        } else {
            System.out.println("Enemy Menang!");
        }
    }
}