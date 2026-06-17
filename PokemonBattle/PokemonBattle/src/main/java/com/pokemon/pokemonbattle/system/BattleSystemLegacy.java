package com.pokemon.pokemonbattle.system;

import java.util.Scanner;
import java.util.ArrayList;

import com.pokemon.pokemonbattle.model.Pokemon;
import com.pokemon.pokemonbattle.model.InventoryItem;
import com.pokemon.pokemonbattle.model.Player;
import com.pokemon.pokemonbattle.data.ItemData;

/**
 * Console-based BattleSystem (for legacy console gameplay)
 * For web-based game, use GameService instead
 */
public class BattleSystemLegacy {

    private Player playerData;
    private Pokemon player;
    private Pokemon enemy;

    private Scanner input = new Scanner(System.in);
    private ArrayList<InventoryItem> inventory;

    private int captureAttempts = 3;

    public BattleSystemLegacy(Player playerData, Pokemon enemy) {
        this.playerData = playerData;
        this.player = playerData.getActivePokemon();
        this.enemy = enemy;

        this.inventory = ItemData.getStartingInventory();
    }

    public void startBattle() {
        System.out.println("=== BATTLE START ===");

        int turn = 1;

        while (player.isAlive() && enemy.isAlive()) {

            System.out.println("\n=== TURN " + turn + " ===");

            if (enemy.getHpRatio() <= 0.2) {
                System.out.println("⚠️ Pokemon bisa ditangkap!");
            }

            System.out.println("\nGiliran Player:");

            player.applyStatusEffect();
            if (!player.isAlive())
                break;

            if (player.canMove()) {

                System.out.println("1. Attack");
                System.out.println("2. Use Item");
                System.out.println("3. Catch Pokemon (" + captureAttempts + ")");
                System.out.print("Pilih aksi: ");

                int action = input.nextInt();

                if (action == 1) {

                    player.showSkills(enemy);

                    System.out.print("Pilih skill: ");
                    int choice = input.nextInt();

                    if (choice < 1 || choice > player.getSkills().size()) {
                        choice = 1;
                    }

                    player.useSkill(choice - 1, enemy);
                }

                else if (action == 3) {

                    if (captureAttempts <= 0) {
                        System.out.println("Pokeball habis!");
                        continue;
                    }

                    if (!enemy.isAlive()) {
                        System.out.println("Tidak bisa ditangkap!");
                        continue;
                    }

                    System.out.println("Melempar Pokeball...");

                    boolean caught = CaptureSystem.tryCapture(enemy);
                    captureAttempts--;

                    if (caught) {
                        playerData.addPokemon(enemy);
                        break;
                    } else {
                        System.out.println("Enemy menyerang!");

                        if (enemy.canMove()) {
                            int rand = (int) (Math.random() * enemy.getSkills().size());
                            enemy.useSkill(rand, player);
                        }
                    }
                } else if (action == 2) {

                    if (inventory == null || inventory.isEmpty()) {
                        System.out.println("Inventory kosong!");
                        continue;
                    }

                    System.out.println("=== INVENTORY ===");

                    for (int i = 0; i < inventory.size(); i++) {
                        System.out.println((i + 1) + ". " + inventory.get(i).getItem().getName());
                    }

                    System.out.print("Pilih item: ");
                    int itemChoice = input.nextInt();

                    if (itemChoice < 1 || itemChoice > inventory.size()) {
                        System.out.println("Pilihan tidak valid!");
                        continue;
                    }

                    InventoryItem selected = inventory.get(itemChoice - 1);
                    player.setHp(Math.min(player.getMaxHp(), player.getHp() + 20));
                    inventory.remove(itemChoice - 1);

                    System.out.println("Gunakan " + selected.getItem().getName() + "!");
                }

                System.out.println("\nGiliran Enemy:");
                enemy.applyStatusEffect();
                if (!enemy.isAlive())
                    break;

                if (enemy.canMove()) {
                    int rand = (int) (Math.random() * enemy.getSkills().size());
                    enemy.useSkill(rand, player);
                }
            }

            turn++;
        }

        if (!player.isAlive()) {
            System.out.println("\n❌ KALAH!");
        } else {
            System.out.println("\n🎉 MENANG!");
        }
    }
}
