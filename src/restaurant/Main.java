package restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import restaurant.api.MealChoice;
import restaurant.api.OrderRequest;
import restaurant.api.OrderingFacade;
import restaurant.api.Receipt;
import restaurant.observer.OrderStatus;

public final class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final OrderingFacade facade = new OrderingFacade(System.out::println);

    public static void main(String[] args) {
        Main app = new Main();
        if (args.length > 0 && "--demo".equals(args[0])) {
            app.runDemo();
        } else {
            app.runInteractive();
        }
    }

    private void runDemo() {
        System.out.println("RESTAURANT ORDERING SYSTEM - SIX PATTERNS DEMO");
        System.out.println(facade.menuText());
        System.out.println();
        Receipt first = facade.placeOrder(new OrderRequest(
                "Aida",
                List.of(new MealChoice("burger", List.of("cheese", "patty")),
                        new MealChoice("pizza", List.of("spicy"))),
                "student", false, 7, "No onions"));
        System.out.println(first.format());
        System.out.println("\nMoving the first order through the kitchen:");
        for (int i = 0; i < 3; i++) {
            facade.advanceOrder(first.order().id());
        }
        System.out.println();
        Receipt second = facade.placeOrder(new OrderRequest(
                "Bek",
                List.of(new MealChoice("wrap", List.of("cheese"))),
                "loyalty", true, 0, "Extra napkins"));
        System.out.println(second.format());
        System.out.println("\nORDER HISTORY:");
        facade.orderSummaries().forEach(System.out::println);
    }

    private void runInteractive() {
        System.out.println("Welcome to Restaurant Ordering System!");
        while (true) {
            System.out.println("\n1. Show menu  2. Place order  3. Update status  4. View orders  5. Exit");
            String selection = ask("Choose an option: ");
            try {
                switch (selection) {
                    case "1" -> System.out.println(facade.menuText());
                    case "2" -> placeOrder();
                    case "3" -> updateStatus();
                    case "4" -> showOrders();
                    case "5" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Choose a number from 1 to 5.");
                }
            } catch (IllegalArgumentException | IllegalStateException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private void placeOrder() {
        System.out.println(facade.menuText());
        String customer = ask("Customer name: ");
        boolean takeaway = ask("Takeaway? (y/n): ").equalsIgnoreCase("y");
        int tableNumber = takeaway ? 0 : readInt("Table number: ", 1, 999);
        int count = readInt("How many meal items (1-10)? ", 1, 10);
        List<MealChoice> meals = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String mealKey = ask("Meal " + i + " key (burger/pizza/wrap): ");
            String rawExtras = ask("Extras separated by commas (cheese/patty/spicy), or none: ");
            List<String> extras = new ArrayList<>();
            if (!rawExtras.isBlank() && !rawExtras.equalsIgnoreCase("none")) {
                for (String extra : rawExtras.split(",")) {
                    extras.add(extra.trim());
                }
            }
            meals.add(new MealChoice(mealKey, extras));
        }
        String discount = ask("Discount (none/student/loyalty): ");
        String notes = ask("Notes (optional): ");
        OrderRequest request = new OrderRequest(customer, meals, discount, takeaway, tableNumber, notes);
        Receipt receipt = facade.placeOrder(request);
        System.out.println(receipt.format());
    }

    private void updateStatus() {
        String orderId = ask("Enter order ID (e.g. ORD-1001): ");
        OrderStatus status = facade.advanceOrder(orderId);
        System.out.println("New status: " + status);
    }

    private void showOrders() {
        List<String> summaries = facade.orderSummaries();
        if (summaries.isEmpty()) {
            System.out.println("No orders yet.");
        } else {
            summaries.forEach(System.out::println);
        }
    }

    private int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            try {
                int value = Integer.parseInt(ask(prompt));
                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Enter a number between " + minimum + " and " + maximum + ".");
        }
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
