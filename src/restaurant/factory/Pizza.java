package restaurant.factory;

public final class Pizza implements Meal {
    @Override
    public String name() {
        return "Margherita Pizza";
    }

    @Override
    public int priceTenge() {
        return 3000;
    }
}
