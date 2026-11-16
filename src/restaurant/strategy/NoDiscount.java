package restaurant.strategy;

public final class NoDiscount implements DiscountStrategy {
    @Override
    public String label() {
        return "No discount";
    }

    @Override
    public int discountTenge(int subtotalTenge) {
        return 0;
    }
}
