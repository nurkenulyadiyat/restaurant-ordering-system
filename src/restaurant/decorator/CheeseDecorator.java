package restaurant.decorator;

import restaurant.factory.Meal;

public final class CheeseDecorator extends MealDecorator {
    public CheeseDecorator(Meal wrapped) {
        super(wrapped);
    }

    @Override
    public String name() {
        return wrapped.name() + " + Cheese";
    }

    @Override
    public int priceTenge() {
        return Math.addExact(wrapped.priceTenge(), 350);
    }
}
