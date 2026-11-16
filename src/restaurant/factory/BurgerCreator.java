package restaurant.factory;

public final class BurgerCreator extends MealCreator {
    @Override
    protected Meal createMeal() {
        return new Burger();
    }
}
