package restaurant.decorator;

import restaurant.factory.Meal;

public final class SpicySauceDecorator extends MealDecorator {
    public SpicySauceDecorator(Meal wrapped) {
        super(wrapped);
    }

    @Override
    public String name() {
        return wrapped.name() + " + Spicy Sauce";
    }

    @Override
    public int priceTenge() {
        return Math.addExact(wrapped.priceTenge(), 250);
    }
}
