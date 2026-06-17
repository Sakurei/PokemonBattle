package data;

import java.util.ArrayList;
import Models.Item;
import Models.InventoryItem;

public class ItemData {

    public static ArrayList<InventoryItem> getStartingInventory() {

        ArrayList<InventoryItem> items = new ArrayList<>();

        items.add(new InventoryItem(new Item("Potion", 30, false, 1.0), 2));   // 2 potion
        items.add(new InventoryItem(new Item("Antidote", 0, true, 0.7), 1));   // 1 antidote

        return items;
    }
}