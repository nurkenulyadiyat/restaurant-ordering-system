package restaurant.strategy;

public interface DiscountStrategy {
    String label();
    int discountTenge(int subtotalTenge);
}
