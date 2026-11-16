# Restaurant Ordering System - Final Project (Java 17)

A small interactive restaurant-ordering application illustrating **six functioning design patterns in one integrated project**. No frameworks, database, or third-party dependencies are required.

## Team

| Team member | Responsibility | Design patterns |
| --- | --- | --- |
| Adiyat Nurkenuly | Meal creation and order construction | Factory Method, Builder |
| Bekarys Zhassuzakh | Meal extras and discounts | Decorator, Strategy |
| Abubakir Daniyaruly | Order status notifications and application integration | Observer, Facade |

## Requirements

- Java Development Kit (JDK) **17 or newer** (`java -version` and `javac -version`).
- Windows, Linux, or macOS.

**No Maven or Gradle is needed.** All amounts are educational demo prices in KZT. Payments and student/loyalty eligibility are simulated rather than integrated with real services.

## Run

On Windows, in the project folder:

```bat
run.bat --demo
run.bat
```

On Linux or macOS:

```sh
sh run.sh --demo
sh run.sh
```

`--demo` runs a deterministic complete order scenario; without it, a text-based menu supports viewing meals, placing orders, moving statuses forward and reviewing order history.

## Tests

Windows:

```bat
test.bat
```

Linux/macOS:

```sh
sh test.sh
```

Tests verify each of the six patterns plus interactions, invalid input and order progression. The tests use no external libraries.

## Six design patterns

| Pattern | Main Java classes | Visible behavior |
| --- | --- | --- |
| **Factory Method** | `MealCreator`, `BurgerCreator`, `PizzaCreator`, `WrapCreator` | Subclasses decide which concrete meal gets constructed. |
| **Builder** | `Order.Builder` | Assemble a validated, immutable order with meals, service mode, table and notes. |
| **Decorator** | `MealDecorator`, `CheeseDecorator`, `ExtraPattyDecorator`, `SpicySauceDecorator` | Combine extras at runtime; each wrapper adds its own price and description. |
| **Strategy** | `DiscountStrategy`, `DiscountCalculator`, `StudentDiscount`, `LoyaltyDiscount` | Discount algorithms are selected and swapped via a shared interface. |
| **Observer** | `OrderTracker`, `OrderObserver`, `CustomerObserver`, `KitchenObserver` | Both customer and kitchen are notified on each order status change. |
| **Facade** | `OrderingFacade` | One client-facing API coordinates factories, decorators, builder, strategy, receipt and notifications. |

## Flow

1. The user chooses one or more meals and optionally extras.
2. A Factory Method creates each concrete meal.
3. Decorators wrap the meals with extras.
4. `Order.Builder` assembles and validates an immutable order.
5. A Strategy computes the selected discount.
6. The Facade stores the receipt and asks the Observer subsystem to broadcast `PLACED`.
7. Subsequent status updates flow through `PREPARING`, `READY`, `COMPLETED` and notify the same subscribers.

No change of state can occur past `COMPLETED`. No invalid meal or extra is added to order history.

## Key files

- `src/restaurant/Main.java` - interactive CLI and reproducible demo.
- `src/restaurant/factory/` and `src/restaurant/builder/` - Adiyat's modules.
- `src/restaurant/decorator/` and `src/restaurant/strategy/` - Bekarys's modules.
- `src/restaurant/observer/` and `src/restaurant/api/` - Abubakir's modules.
- `tests/restaurant/ProjectTests.java` - automated checks.
- `docs/class-diagram.png`, `docs/order-flow.png` - UML and system flow diagrams.
- `docs/demo-output.png` - image generated from actual demo console output.
- `docs/team-contributions.md` - proposed integration plan and defense pointers.

## Example scenario

The demo creates an order for Aida containing a burger with cheese + extra patty and a pizza with spicy sauce; the order is discounted by 10% and moved through its complete life cycle. It then creates a takeaway wrap order for Bek with a loyalty discount.

## Submission

Use the **single complete source ZIP** together with the **separate PDF report** in Moodle. The three teammate ZIPs are development handoff packages, **not three independent final projects**. Each student should implement, review and be able to explain their assigned patterns before defense. Do not claim unperformed work or invented commits.
