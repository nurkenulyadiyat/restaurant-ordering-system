package restaurant.factory;

public final class PizzaCreator extends MealCreator {
    @Override
    protected Meal createMeal() {
        return new Pizza();
    }
}
