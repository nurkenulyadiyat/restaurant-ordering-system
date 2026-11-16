package restaurant.observer;

import java.util.Objects;
import java.util.function.Consumer;
import restaurant.builder.Order;

public final class CustomerObserver implements OrderObserver {
    private final Consumer<String> output;

    public CustomerObserver(Consumer<String> output) {
        this.output = Objects.requireNonNull(output);
    }

    @Override
    public void onStatusChanged(Order order, OrderStatus status) {
        output.accept("[Customer] " + order.customer() + ": " + order.id() + " is " + status + ".");
    }
}
