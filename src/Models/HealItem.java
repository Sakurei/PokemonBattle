package Models;

public class HealItem extends Items {
    private int healAmount;

    public HealItem(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
    }

    @Override
    public void use(Pokemon target) {
        target.heal(healAmount);

        System.out.println(target.getName() + " di-heal sebesar " + healAmount 
            + " | HP sekarang: " + target.getHp());
    }
}
