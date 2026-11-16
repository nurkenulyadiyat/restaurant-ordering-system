package restaurant.factory;

public final class Wrap implements Meal {
    @Override
    public String name() {
        return "Chicken Wrap";
    }

    @Override
    public int priceTenge() {
        return 2000;
    }
}
