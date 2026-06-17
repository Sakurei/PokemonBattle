package battles;

import java.util.Scanner;
import java.util.ArrayList;

import Models.Pokemon;
import Models.InventoryItem;
import Models.Player;

public class BattleSystem {

    private Player playerData;
    private Pokemon player;
    private Pokemon enemy;

    private Scanner input = new Scanner(System.in);
    private ArrayList<InventoryItem> inventory;

    private int captureAttempts = 3;

    public BattleSystem(Player playerData, Pokemon enemy) {
        this.playerData = playerData;
        this.player = playerData.getActivePokemon();
        this.enemy = enemy;

        this.inventory = data.ItemData.getStartingInventory();
    }

    public void startBattle() {
        System.out.println("=== BATTLE START ===");

        int turn = 1;

        while (player.isAlive() && enemy.isAlive()) {

            System.out.println("\n=== TURN " + turn + " ===");

            // 🔥 indikator tangkap
            if (enemy.getHpRatio() <= 0.2) {
                System.out.println("⚠️ Pokemon bisa ditangkap!");
            }

            // ===== PLAYER =====
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

                    boolean caught = system.CaptureSystem.tryCapture(enemy);
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

                    // 🔥 pakai item
                    InventoryItem selected = inventory.get(itemChoice - 1);
                    selected.getItem().use(player);
                }
            }

            if (!enemy.isAlive())
                break;

            // ===== ENEMY =====
            System.out.println("\nGiliran Enemy:");

            enemy.applyStatusEffect();
            if (!enemy.isAlive())
                break;

            if (enemy.canMove()) {
                int rand = (int) (Math.random() * enemy.getSkills().size());
                enemy.useSkill(rand, player);
            }

            turn++;
        }

        System.out.println("\n=== BATTLE END ===");

        if (player.isAlive()) {
            System.out.println("Player Menang!");
        } else {
            System.out.println("Enemy Menang!");
        }
    }
}