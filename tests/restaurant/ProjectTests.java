package restaurant;

import java.util.ArrayList;
import java.util.List;
import restaurant.api.MealChoice;
import restaurant.api.OrderRequest;
import restaurant.api.OrderingFacade;
import restaurant.api.Receipt;
import restaurant.builder.Order;
import restaurant.decorator.CheeseDecorator;
import restaurant.decorator.ExtraPattyDecorator;
import restaurant.decorator.ExtrasService;
import restaurant.factory.Burger;
import restaurant.factory.BurgerCreator;
import restaurant.factory.Meal;
import restaurant.factory.Pizza;
import restaurant.factory.PizzaCreator;
import restaurant.observer.OrderStatus;
import restaurant.observer.OrderTracker;
import restaurant.strategy.DiscountCalculator;
import restaurant.strategy.LoyaltyDiscount;
import restaurant.strategy.NoDiscount;
import restaurant.strategy.StudentDiscount;

public final class ProjectTests {
    private static int passed;

    public static void main(String[] args) {
        testFactoryMethod();
        testBuilder();
        testBuilderValidation();
        testDecorator();
        testDecoratorInvalid();
        testStrategy();
        testObserver();
        testObserverUnsubscribe();
        testFacadeIntegration();
        testFacadeRejection();
        testFacadeStatuses();
        System.out.println("ALL TESTS PASSED: " + passed);
    }

    private static void testFactoryMethod() {
        check(new BurgerCreator().prepareMeal() instanceof Burger, "Factory creates Burger");
        check(new PizzaCreator().prepareMeal() instanceof Pizza, "Factory creates Pizza");
    }

    private static void testBuilder() {
        Order order = new Order.Builder("ORD-1", "Aida")
                .dineIn(4)
                .addMeal(new Burger())
                .addMeal(new Pizza())
                .notes("No salt")
                .build();
        check(order.subtotalTenge() == 5400 && order.tableNumber() == 4
                && !order.isTakeaway() && order.meals().size() == 2, "Builder creates complete immutable order");
        expectException(UnsupportedOperationException.class, () -> order.meals().clear(), "Builder locks item list");
    }

    private static void testBuilderValidation() {
        expectException(IllegalArgumentException.class,
                () -> new Order.Builder("ORD-2", "Aida").build(), "Builder rejects empty order");
        expectException(IllegalArgumentException.class,
                () -> new Order.Builder("ORD-3", "Aida").dineIn(0), "Builder rejects invalid table");
    }

    private static void testDecorator() {
        Meal meal = new ExtraPattyDecorator(new CheeseDecorator(new Burger()));
        check(meal.priceTenge() == 3650 && meal.name().contains("Cheese")
                && meal.name().contains("Extra Patty"), "Decorator stacks extras");
        check(new Burger().priceTenge() == 2400, "Decorator does not change original meal");
    }

    private static void testDecoratorInvalid() {
        expectException(IllegalArgumentException.class,
                () -> new ExtrasService().apply(new Burger(), "unknown"), "Decorator rejects unknown extra");
    }

    private static void testStrategy() {
        DiscountCalculator calculator = new DiscountCalculator(new NoDiscount());
        check(calculator.calculateDiscount(1000) == 0, "Strategy no discount");
        calculator.setStrategy(new StudentDiscount());
        check(calculator.calculateDiscount(1000) == 100, "Strategy dynamically switches to student");
        calculator.setStrategy(new LoyaltyDiscount());
        check(calculator.calculateDiscount(1000) == 150, "Strategy dynamically switches to loyalty");
    }

    private static void testObserver() {
        OrderTracker tracker = new OrderTracker();
        List<OrderStatus> events = new ArrayList<>();
        tracker.subscribe((order, status) -> events.add(status));
        Order order = new Order.Builder("ORD-4", "Daniyar")
                .addMeal(new Burger()).build();
        tracker.place(order);
        tracker.advance(order);
        check(events.equals(List.of(OrderStatus.PLACED, OrderStatus.PREPARING)), "Observer receives status events");
    }

    private static void testObserverUnsubscribe() {
        OrderTracker tracker = new OrderTracker();
        List<OrderStatus> events = new ArrayList<>();
        restaurant.observer.OrderObserver subscriber = (order, status) -> events.add(status);
        tracker.subscribe(subscriber);
        tracker.unsubscribe(subscriber);
        tracker.place(new Order.Builder("ORD-5", "Bek").addMeal(new Pizza()).build());
        check(events.isEmpty(), "Observer unsubscribes correctly");
    }

    private static void testFacadeIntegration() {
        List<String> notifications = new ArrayList<>();
        OrderingFacade facade = new OrderingFacade(notifications::add);
        Receipt receipt = facade.placeOrder(new OrderRequest("Aida",
                List.of(new MealChoice("burger", List.of("cheese", "patty")),
                        new MealChoice("pizza", List.of("spicy"))),
                "student", false, 8, "No onion"));
        check(receipt.order().subtotalTenge() == 6900, "Facade integrates factory builder decorator");
        check(receipt.discountTenge() == 690 && receipt.totalTenge() == 6210,
                "Facade integrates strategy discount");
        check(notifications.size() == 2 && notifications.get(0).contains("PLACED")
                && notifications.get(1).contains("PLACED"), "Facade registers two observers");
        check(facade.menuText().contains("Classic Burger"), "Facade exposes simplified menu");
    }

    private static void testFacadeRejection() {
        OrderingFacade facade = new OrderingFacade(message -> {});
        expectException(IllegalArgumentException.class,
                () -> facade.placeOrder(new OrderRequest("Aida",
                        List.of(new MealChoice("bad", List.of())), "none", true, 0, "")),
                "Facade rejects unknown meal");
        expectException(IllegalArgumentException.class,
                () -> facade.placeOrder(new OrderRequest("Aida",
                        List.of(new MealChoice("burger", List.of("bad"))), "none", true, 0, "")),
                "Facade rejects unknown extra");
        check(facade.orderSummaries().isEmpty(), "Invalid orders do not enter order history");
    }

    private static void testFacadeStatuses() {
        OrderingFacade facade = new OrderingFacade(message -> {});
        Receipt receipt = facade.placeOrder(new OrderRequest("Bek",
                List.of(new MealChoice("wrap", List.of())), "none", true, 0, ""));
        String id = receipt.order().id();
        check(facade.statusOf(id) == OrderStatus.PLACED, "Facade starts at PLACED");
        facade.advanceOrder(id);
        facade.advanceOrder(id);
        facade.advanceOrder(id);
        check(facade.statusOf(id) == OrderStatus.COMPLETED, "Facade progresses to COMPLETED");
        expectException(IllegalStateException.class, () -> facade.advanceOrder(id),
                "Completed order cannot progress further");
    }

    private static void check(boolean condition, String label) {
        if (!condition) {
            throw new AssertionError("FAILED: " + label);
        }
        passed++;
        System.out.println("PASS: " + label);
    }

    private static void expectException(Class<? extends Throwable> expected,
                                        Runnable action, String label) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (expected.isInstance(actual)) {
                passed++;
                System.out.println("PASS: " + label);
                return;
            }
            throw new AssertionError("Unexpected exception in " + label, actual);
        }
        throw new AssertionError("Expected " + expected.getSimpleName() + " in " + label);
    }
}
