package restaurant.strategy;

public final class StudentDiscount implements DiscountStrategy {
    @Override
    public String label() {
        return "Student discount (10%)";
    }

    @Override
    public int discountTenge(int subtotalTenge) {
        return subtotalTenge / 10;
    }
}
