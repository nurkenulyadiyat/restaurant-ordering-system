package restaurant.api;

import java.util.List;
import java.util.Objects;

public record OrderRequest(
        String customer,
        List<MealChoice> meals,
        String discountKey,
        boolean takeaway,
        int tableNumber,
        String notes) {
    public OrderRequest {
        Objects.requireNonNull(customer, "Customer is required.");
        Objects.requireNonNull(meals, "Meals are required.");
        Objects.requireNonNull(discountKey, "Discount is required.");
        meals = List.copyOf(meals);
        notes = notes == null ? "" : notes;
    }
}
