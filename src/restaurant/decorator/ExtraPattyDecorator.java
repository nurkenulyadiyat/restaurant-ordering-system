package restaurant.decorator;

import restaurant.factory.Meal;

public final class ExtraPattyDecorator extends MealDecorator {
    public ExtraPattyDecorator(Meal wrapped) {
        super(wrapped);
    }

    @Override
    public String name() {
        return wrapped.name() + " + Extra Patty";
    }

    @Override
    public int priceTenge() {
        return Math.addExact(wrapped.priceTenge(), 900);
    }
}
