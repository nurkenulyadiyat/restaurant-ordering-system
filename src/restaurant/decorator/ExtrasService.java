package restaurant.decorator;

import java.util.List;
import java.util.Locale;
import restaurant.factory.Meal;

public final class ExtrasService {
    public Meal apply(Meal meal, String key) {
        if (key == null) {
            throw new IllegalArgumentException("Extra name cannot be null.");
        }
        return switch (key.trim().toLowerCase(Locale.ROOT)) {
            case "cheese" -> new CheeseDecorator(meal);
            case "patty" -> new ExtraPattyDecorator(meal);
            case "spicy" -> new SpicySauceDecorator(meal);
            default -> throw new IllegalArgumentException("Unknown extra: " + key);
        };
    }

    public List<String> availableExtras() {
        return List.of("cheese (+350 KZT)", "patty (+900 KZT)", "spicy (+250 KZT)");
    }
}
