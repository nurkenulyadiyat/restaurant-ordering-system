package restaurant.decorator;

import java.util.Objects;
import restaurant.factory.Meal;

public abstract class MealDecorator implements Meal {
    protected final Meal wrapped;

    protected MealDecorator(Meal wrapped) {
        this.wrapped = Objects.requireNonNull(wrapped);
    }
}
