package restaurant.observer;

import java.util.Objects;
import java.util.function.Consumer;
import restaurant.builder.Order;

public final class KitchenObserver implements OrderObserver {
    private final Consumer<String> output;

    public KitchenObserver(Consumer<String> output) {
        this.output = Objects.requireNonNull(output);
    }

    @Override
    public void onStatusChanged(Order order, OrderStatus status) {
        output.accept("[Kitchen] " + order.id() + " -> " + status + " (" + order.meals().size() + " item(s)).");
    }
}
