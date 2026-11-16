package restaurant.api;

import restaurant.builder.Order;
import restaurant.factory.Meal;

public final class Receipt {
    private final Order order;
    private final String discountLabel;
    private final int discountTenge;

    public Receipt(Order order, String discountLabel, int discountTenge) {
        this.order = order;
        this.discountLabel = discountLabel;
        this.discountTenge = discountTenge;
    }

    public Order order() {
        return order;
    }

    public int discountTenge() {
        return discountTenge;
    }

    public int totalTenge() {
        return order.subtotalTenge() - discountTenge;
    }

    public String format() {
        StringBuilder text = new StringBuilder();
        text.append("========== ORDER RECEIPT ==========\n");
        text.append("Order ID: ").append(order.id()).append('\n');
        text.append("Customer: ").append(order.customer()).append('\n');
        text.append("Service: ").append(order.isTakeaway() ? "Takeaway" : "Table " + order.tableNumber()).append('\n');
        for (int i = 0; i < order.meals().size(); i++) {
            Meal meal = order.meals().get(i);
            text.append(i + 1).append(". ").append(meal.name()).append(" | ").append(meal.priceTenge()).append(" KZT\n");
        }
        if (!order.notes().isEmpty()) {
            text.append("Notes: ").append(order.notes()).append('\n');
        }
        text.append("Subtotal: ").append(order.subtotalTenge()).append(" KZT\n");
        text.append("Discount: ").append(discountLabel).append(" (-").append(discountTenge).append(" KZT)\n");
        text.append("TOTAL: ").append(totalTenge()).append(" KZT\n");
        text.append("===================================");
        return text.toString();
    }
}
