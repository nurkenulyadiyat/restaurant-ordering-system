package restaurant.factory;

public final class Burger implements Meal {
    @Override
    public String name() {
        return "Classic Burger";
    }

    @Override
    public int priceTenge() {
        return 2400;
    }
}
