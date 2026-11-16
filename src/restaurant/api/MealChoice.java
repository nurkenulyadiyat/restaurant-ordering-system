package restaurant.api;

import java.util.List;
import java.util.Objects;

public record MealChoice(String mealKey, List<String> extras) {
    public MealChoice {
        Objects.requireNonNull(mealKey, "Meal selection is required.");
        Objects.requireNonNull(extras, "Extras list is required.");
        extras = List.copyOf(extras);
    }
}
