package data;

import java.util.ArrayList;
import Models.Item;

public class ItemData {

    public static ArrayList<Item> getAllItems() {

        ArrayList<Item> items = new ArrayList<>();

        // Potion → heal selalu berhasil
        items.add(new Item("Potion", 30, false, 1.0));

        // Antidote → 70% berhasil
        items.add(new Item("Antidote", 0, true, 0.7));

        return items;
    }
}