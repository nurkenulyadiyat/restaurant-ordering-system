package restaurant.strategy;

import java.util.Objects;

public final class DiscountCalculator {
    private DiscountStrategy strategy;

    public DiscountCalculator(DiscountStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public int calculateDiscount(int subtotalTenge) {
        if (subtotalTenge < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative.");
        }
        int discount = strategy.discountTenge(subtotalTenge);
        if (discount < 0 || discount > subtotalTenge) {
            throw new IllegalStateException("Invalid discount returned by strategy.");
        }
        return discount;
    }

    public String strategyLabel() {
        return strategy.label();
    }
}
