package restaurant.strategy;

public final class LoyaltyDiscount implements DiscountStrategy {
    @Override
    public String label() {
        return "Loyalty discount (15%)";
    }

    @Override
    public int discountTenge(int subtotalTenge) {
        return subtotalTenge * 15 / 100;
    }
}
