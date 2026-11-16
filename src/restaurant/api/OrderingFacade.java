package restaurant.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import restaurant.builder.Order;
import restaurant.decorator.ExtrasService;
import restaurant.factory.BurgerCreator;
import restaurant.factory.Meal;
import restaurant.factory.MealCreator;
import restaurant.factory.PizzaCreator;
import restaurant.factory.WrapCreator;
import restaurant.observer.CustomerObserver;
import restaurant.observer.KitchenObserver;
import restaurant.observer.OrderStatus;
import restaurant.observer.OrderTracker;
import restaurant.strategy.DiscountCalculator;
import restaurant.strategy.DiscountStrategies;
import restaurant.strategy.NoDiscount;

public final class OrderingFacade {
    private final Map<String, MealCreator> creators = new LinkedHashMap<>();
    private final Map<String, Receipt> receipts = new LinkedHashMap<>();
    private final AtomicInteger nextOrderNumber = new AtomicInteger(1001);
    private final ExtrasService extrasService = new ExtrasService();
    private final DiscountCalculator discountCalculator = new DiscountCalculator(new NoDiscount());
    private final OrderTracker orderTracker = new OrderTracker();

    public OrderingFacade(Consumer<String> notificationOutput) {
        Consumer<String> output = Objects.requireNonNull(notificationOutput);
        creators.put("burger", new BurgerCreator());
        creators.put("pizza", new PizzaCreator());
        creators.put("wrap", new WrapCreator());
        orderTracker.subscribe(new CustomerObserver(output));
        orderTracker.subscribe(new KitchenObserver(output));
    }

    public String menuText() {
        StringBuilder text = new StringBuilder("MENU (enter a key):\n");
        for (Map.Entry<String, MealCreator> entry : creators.entrySet()) {
            Meal meal = entry.getValue().prepareMeal();
            text.append("  ").append(entry.getKey()).append(" - ")
                    .append(meal.name()).append(" (" ).append(meal.priceTenge()).append(" KZT)\n");
        }
        text.append("EXTRAS:\n");
        for (String extra : extrasService.availableExtras()) {
            text.append("  ").append(extra).append('\n');
        }
        text.append("DISCOUNTS: none, student (10%), loyalty (15%)");
        return text.toString();
    }

    public Receipt placeOrder(OrderRequest request) {
        Objects.requireNonNull(request, "Request is required.");
        if (request.meals().isEmpty()) {
            throw new IllegalArgumentException("Choose at least one meal.");
        }
        discountCalculator.setStrategy(DiscountStrategies.choose(request.discountKey()));
        List<Meal> chosenMeals = new ArrayList<>();
        for (MealChoice choice : request.meals()) {
            MealCreator creator = creators.get(choice.mealKey().trim().toLowerCase(Locale.ROOT));
            if (creator == null) {
                throw new IllegalArgumentException("Unknown meal: " + choice.mealKey());
            }
            Meal meal = creator.prepareMeal();
            for (String extra : choice.extras()) {
                meal = extrasService.apply(meal, extra);
            }
            chosenMeals.add(meal);
        }
        String id = "ORD-" + nextOrderNumber.getAndIncrement();
        Order.Builder builder = new Order.Builder(id, request.customer()).notes(request.notes());
        if (request.takeaway()) {
            builder.takeaway();
        } else {
            builder.dineIn(request.tableNumber());
        }
        for (Meal meal : chosenMeals) {
            builder.addMeal(meal);
        }
        Order order = builder.build();
        int discount = discountCalculator.calculateDiscount(order.subtotalTenge());
        Receipt receipt = new Receipt(order, discountCalculator.strategyLabel(), discount);
        receipts.put(id, receipt);
        orderTracker.place(order);
        return receipt;
    }

    public OrderStatus advanceOrder(String orderId) {
        Receipt receipt = findReceipt(orderId);
        return orderTracker.advance(receipt.order());
    }

    public OrderStatus statusOf(String orderId) {
        findReceipt(orderId);
        return orderTracker.statusOf(orderId);
    }

    public Receipt findReceipt(String orderId) {
        Receipt receipt = receipts.get(orderId);
        if (receipt == null) {
            throw new IllegalArgumentException("Unknown order: " + orderId);
        }
        return receipt;
    }

    public List<String> orderSummaries() {
        List<String> summaries = new ArrayList<>();
        for (Receipt receipt : receipts.values()) {
            Order order = receipt.order();
            summaries.add(order.id() + " | " + order.customer() + " | "
                    + orderTracker.statusOf(order.id()) + " | " + receipt.totalTenge() + " KZT");
        }
        return List.copyOf(summaries);
    }
}
