package system;

import java.io.FileWriter;
import java.io.IOException;

import Models.Player;
import Models.Pokemon;

public class SaveSystem {

    public static void savePlayer(Player player) {

        try {
            FileWriter writer = new FileWriter("save.txt");

            for (Pokemon p : player.getCollection()) {
                writer.write(
                    p.getName() + "," +
                    p.getHp() + "," +
                    p.getAttack() + "," +
                    p.getType() + "," +
                    p.isLegendary() + "\n"
                );
            }

            writer.close();
            System.out.println("Game berhasil disimpan!");

        } catch (IOException e) {
            System.out.println("Gagal save!");
        }
    }
}