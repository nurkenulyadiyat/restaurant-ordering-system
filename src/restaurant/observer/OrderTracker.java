package restaurant.observer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import restaurant.builder.Order;

public final class OrderTracker {
    private final List<OrderObserver> observers = new ArrayList<>();
    private final Map<String, OrderStatus> statuses = new LinkedHashMap<>();

    public void subscribe(OrderObserver observer) {
        observers.add(Objects.requireNonNull(observer));
    }

    public void unsubscribe(OrderObserver observer) {
        observers.remove(observer);
    }

    public void place(Order order) {
        Objects.requireNonNull(order);
        if (statuses.containsKey(order.id())) {
            throw new IllegalArgumentException("Order already exists: " + order.id());
        }
        statuses.put(order.id(), OrderStatus.PLACED);
        notifyObservers(order, OrderStatus.PLACED);
    }

    public OrderStatus advance(Order order) {
        Objects.requireNonNull(order);
        OrderStatus current = statuses.get(order.id());
        if (current == null) {
            throw new IllegalArgumentException("Order was not placed: " + order.id());
        }
        OrderStatus next = switch (current) {
            case PLACED -> OrderStatus.PREPARING;
            case PREPARING -> OrderStatus.READY;
            case READY -> OrderStatus.COMPLETED;
            case COMPLETED -> throw new IllegalStateException("Order is already completed.");
        };
        statuses.put(order.id(), next);
        notifyObservers(order, next);
        return next;
    }

    public OrderStatus statusOf(String orderId) {
        OrderStatus status = statuses.get(orderId);
        if (status == null) {
            throw new IllegalArgumentException("Unknown order: " + orderId);
        }
        return status;
    }

    private void notifyObservers(Order order, OrderStatus status) {
        for (OrderObserver observer : List.copyOf(observers)) {
            observer.onStatusChanged(order, status);
        }
    }
}
