package restaurant.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import restaurant.factory.Meal;

public final class Order {
    private final String id;
    private final String customer;
    private final List<Meal> meals;
    private final boolean takeaway;
    private final int tableNumber;
    private final String notes;

    private Order(Builder builder) {
        this.id = builder.id;
        this.customer = builder.customer;
        this.meals = List.copyOf(builder.meals);
        this.takeaway = builder.takeaway;
        this.tableNumber = builder.tableNumber;
        this.notes = builder.notes;
    }

    public String id() {
        return id;
    }

    public String customer() {
        return customer;
    }

    public List<Meal> meals() {
        return meals;
    }

    public boolean isTakeaway() {
        return takeaway;
    }

    public int tableNumber() {
        return tableNumber;
    }

    public String notes() {
        return notes;
    }

    public int subtotalTenge() {
        int subtotal = 0;
        for (Meal meal : meals) {
            subtotal = Math.addExact(subtotal, meal.priceTenge());
        }
        return subtotal;
    }

    public static final class Builder {
        private final String id;
        private final String customer;
        private final List<Meal> meals = new ArrayList<>();
        private boolean takeaway = true;
        private int tableNumber;
        private String notes = "";

        public Builder(String id, String customer) {
            if (id == null || id.isBlank() || customer == null || customer.isBlank()) {
                throw new IllegalArgumentException("Order ID and customer are required.");
            }
            this.id = id.trim();
            this.customer = customer.trim();
        }

        public Builder addMeal(Meal meal) {
            meals.add(Objects.requireNonNull(meal, "Meal cannot be null."));
            return this;
        }

        public Builder dineIn(int tableNumber) {
            if (tableNumber <= 0) {
                throw new IllegalArgumentException("Table number must be positive.");
            }
            this.takeaway = false;
            this.tableNumber = tableNumber;
            return this;
        }

        public Builder takeaway() {
            this.takeaway = true;
            this.tableNumber = 0;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes == null ? "" : notes.trim();
            return this;
        }

        public Order build() {
            if (meals.isEmpty()) {
                throw new IllegalArgumentException("An order must contain at least one meal.");
            }
            return new Order(this);
        }
    }
}
