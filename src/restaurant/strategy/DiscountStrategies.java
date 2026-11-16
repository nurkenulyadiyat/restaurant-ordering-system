package restaurant.strategy;

import java.util.Locale;

public final class DiscountStrategies {
    private DiscountStrategies() {
    }

    public static DiscountStrategy choose(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Discount name cannot be null.");
        }
        return switch (key.trim().toLowerCase(Locale.ROOT)) {
            case "none" -> new NoDiscount();
            case "student" -> new StudentDiscount();
            case "loyalty" -> new LoyaltyDiscount();
            default -> throw new IllegalArgumentException("Unknown discount: " + key);
        };
    }
}
