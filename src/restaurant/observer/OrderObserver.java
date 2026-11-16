package restaurant.observer;

import restaurant.builder.Order;

public interface OrderObserver {
    void onStatusChanged(Order order, OrderStatus status);
}
