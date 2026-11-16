package restaurant.factory;

public final class WrapCreator extends MealCreator {
    @Override
    protected Meal createMeal() {
        return new Wrap();
    }
}
