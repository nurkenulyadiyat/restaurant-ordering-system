package restaurant.factory;

public abstract class MealCreator {
    protected abstract Meal createMeal();

    public final Meal prepareMeal() {
        Meal meal = createMeal();
        if (meal == null || meal.priceTenge() <= 0) {
            throw new IllegalStateException("A factory must create a valid meal.");
        }
        return meal;
    }
}
