package Models;

public class InventoryItem {

    private Item item;
    private int quantity;

    public InventoryItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decreaseQuantity() {
        quantity--;
    }

    public boolean isEmpty() {
        return quantity <= 0;
    }
}