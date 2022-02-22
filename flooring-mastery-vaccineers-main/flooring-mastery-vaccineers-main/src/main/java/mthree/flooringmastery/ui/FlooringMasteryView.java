/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * View layer. Contains all interactions with the user.
 *
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
@Component
public class FlooringMasteryView {

    private UserIO io;

    /**
     * Provided length of longest sample output line
     */
    private final int BANNER_WIDTH = 69;

    /**
     * Character to display for each banner
     */
    private final String BANNER_CHAR = "=";

    @Autowired
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    /**
     * Shows all options and asks for input
     *
     * @return input option
     */
    public int printMenuAndGetSelection() {
        displayLine();
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*");
        displayLine();

        return io.readInt("Choose from menu above: ", 1, 6);
    }

    /**
     * Prompts user to add an order
     *
     * @param states The available states
     * @param products The available products
     * @return a minimum order with customer name, state, product type, and area
     */
    public Order addOrder(List<State> states, List<Product> products) {
        Order build = new Order();
        displayLine("-");
        String name = readCustomerName("Enter customer name: ", false);
        displayLine("-");
        io.print("States");
        for (int i = 0; i < states.size(); i++) {
            io.print((i + 1) + ") " + states.get(i).getStateAbbreviation());
        }

        String state = states.get(io.readInt("Enter option:", 1, states.size()) - 1).getStateAbbreviation();
        displayLine("-");
        io.print("Product types");
        for (int i = 0; i < products.size(); i++) {
            io.print((i + 1) + ") " + products.get(i).getProductType() + " - $" + products.get(i).getCostPerSquareFoot() + "/ft^2");
        }

        String product = products.get(io.readInt("Enter option:", 1, products.size()) - 1).getProductType();
        displayLine("-");
        BigDecimal area;
        while (true) {
            area = io.readBigDecimal("Enter area (ft^2): ", false);
            if (area.compareTo(new BigDecimal("100")) >= 0) {
                break;
            }
        }
        build.setCustomerName(name);
        build.setState(state);
        build.setProductType(product);

        build.setArea(area);

        return build;
    }

    private String readCustomerName(String prompt, boolean canBeEmpty) {
        String name = "";
        while (true) {
            name = io.readString(prompt);
            if (name.matches("[a-zA-Z0-9 \\.]+") || (name.trim().isEmpty() && canBeEmpty)) {
                break;
            } else {
                io.print("Name must be numbers, letters, dots, or space");
            }
        }
        return name;
    }

    public void displayOrderSummary() {
        displayLine("-");
        io.print("Order summary");
        displayLine("-");
    }

    public void displayOrderNumber(int orderNum) {
        displayLine("=");
        io.print("Your order number is " + orderNum);
        displayLine("=");
    }

    public void displayErrorMessage(String m) {
        io.print(m);
    }

    public LocalDate getDate(String prompt) {
        return io.readDate(prompt + "\n" + genLine("-"));
    }

    public LocalDate getFutureDate(String prompt) {
        return io.readFutureDate(prompt + "\n" + genLine("-"));
    }

    public int getOrderNumber() {
        return io.readInt("Enter an order number: ", 0, Integer.MAX_VALUE);
    }

    public Order getEditOrder(Order old, List<State> states, List<Product> products) {
        Order newOrder = new Order();
        displayLine("-");
        String name = readCustomerName("Enter the name (" + old.getCustomerName() + "): ", true);
        displayLine("-");
        io.print("State (" + old.getState() + "): ");
        for (int i = 0; i < states.size(); i++) {
            io.print((i + 1) + ") " + states.get(i).getStateAbbreviation());
        }
        String userInput = null;
        String state = null;
        do {
            userInput = io.readString("Enter option: ");
            if (userInput.isEmpty()) {
                state = old.getState();
                break;
            }
            try {
                int option = Integer.parseInt(userInput);
                if (option >= 1 && option <= states.size()) {
                    state = states.get(option - 1).getStateAbbreviation();
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                displayErrorMessage("Enter a valid option or press enter to keep old data");
            }
        } while (true);
        displayLine("-");

        io.print("Product type (" + old.getProductType() + ")");
        for (int i = 0; i < products.size(); i++) {
            io.print((i + 1) + ") " + products.get(i).getProductType() + " - $" + products.get(i).getCostPerSquareFoot() + "/ft^2");
        }

        String productType = null;
        do {
            userInput = io.readString("Enter option: ");
            if (userInput.isEmpty()) {
                productType = old.getProductType();
                break;
            }
            try {
                int option = Integer.parseInt(userInput);
                if (option >= 1 && option <= products.size()) {
                    productType = products.get(option - 1).getProductType();
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                displayErrorMessage("Enter a valid option or press enter to keep old data");
            }
        } while (true);

        displayLine("-");
        BigDecimal area;
        while (true) {
            area = io.readBigDecimal("Enter area (" + old.getArea().toString() + "ft^2): ", true);
            if (area == null || area.compareTo(new BigDecimal("100")) >= 0) {
                break;
            } else {
                io.print("Area must be a minimum of 100ft^2.");
            }
        }
        displayLine("-");
        newOrder.setCustomerName(name);
        newOrder.setState(state);
        newOrder.setProductType(productType);
        newOrder.setArea(area);

        return newOrder;
    }

    public void thanksForWastingMyTime() {
        displayLine("-");
        String waste = " _________  ___   ___   ________   ___   __    ___   ___   ______            \n"
                + "/________/\\/__/\\ /__/\\ /_______/\\ /__/\\ /__/\\ /___/\\/__/\\ /_____/\\           \n"
                + "\\__.::.__\\/\\::\\ \\\\  \\ \\\\::: _  \\ \\\\::\\_\\\\  \\ \\\\::.\\ \\\\ \\ \\\\::::_\\/_          \n"
                + "   \\::\\ \\   \\::\\/_\\ .\\ \\\\::(_)  \\ \\\\:. `-\\  \\ \\\\:: \\/_) \\ \\\\:\\/___/\\         \n"
                + "    \\::\\ \\   \\:: ___::\\ \\\\:: __  \\ \\\\:. _    \\ \\\\:. __  ( ( \\_::._\\:\\        \n"
                + "     \\::\\ \\   \\: \\ \\\\::\\ \\\\:.\\ \\  \\ \\\\. \\`-\\  \\ \\\\: \\ )  \\ \\  /____\\:\\       \n"
                + "      \\__\\/    \\__\\/ \\::\\/ \\__\\/\\__\\/ \\__\\/ \\__\\/ \\__\\/\\__\\/  \\_____\\/       \n"
                + " ______   ______   ______                                                    \n"
                + "/_____/\\ /_____/\\ /_____/\\                                                   \n"
                + "\\::::_\\/_\\:::_ \\ \\\\:::_ \\ \\                                                  \n"
                + " \\:\\/___/\\\\:\\ \\ \\ \\\\:(_) ) )_                                                \n"
                + "  \\:::._\\/ \\:\\ \\ \\ \\\\: __ `\\ \\                                               \n"
                + "   \\:\\ \\    \\:\\_\\ \\ \\\\ \\ `\\ \\ \\                                              \n"
                + " __ \\_\\/_   _\\_____\\/ \\_\\/_\\_\\/ _________  ________  ___   __    _______     \n"
                + "/_//_//_/\\ /_______/\\ /_____/\\ /________/\\/_______/\\/__/\\ /__/\\ /______/\\    \n"
                + "\\:\\\\:\\\\:\\ \\\\::: _  \\ \\\\::::_\\/_\\__.::.__\\/\\__.::._\\/\\::\\_\\\\  \\ \\\\::::__\\/__  \n"
                + " \\:\\\\:\\\\:\\ \\\\::(_)  \\ \\\\:\\/___/\\  \\::\\ \\     \\::\\ \\  \\:. `-\\  \\ \\\\:\\ /____/\\ \n"
                + "  \\:\\\\:\\\\:\\ \\\\:: __  \\ \\\\_::._\\:\\  \\::\\ \\    _\\::\\ \\__\\:. _    \\ \\\\:\\\\_  _\\/ \n"
                + "   \\:\\\\:\\\\:\\ \\\\:.\\ \\  \\ \\ /____\\:\\  \\::\\ \\  /__\\::\\__/\\\\. \\`-\\  \\ \\\\:\\_\\ \\ \\ \n"
                + " ___\\_______\\/_\\__\\/\\__\\/ \\_____\\/   \\__\\/  \\________\\/ \\__\\/ \\__\\/ \\_____\\/ \n"
                + "/__//_//_/\\ /_/\\/_/\\                                                         \n"
                + "\\::\\| \\| \\ \\\\ \\ \\ \\ \\                                                        \n"
                + " \\:.      \\ \\\\:\\_\\ \\ \\                                                       \n"
                + "  \\:.\\-/\\  \\ \\\\::::_\\/                                                       \n"
                + "   \\. \\  \\  \\ \\ \\::\\ \\                                                       \n"
                + " ___\\__\\/_\\__\\/__\\__\\/___ __ __   ______                                     \n"
                + "/________/\\/_______/\\/__//_//_/\\ /_____/\\                                    \n"
                + "\\__.::.__\\/\\__.::._\\/\\::\\| \\| \\ \\\\::::_\\/_                                   \n"
                + "   \\::\\ \\     \\::\\ \\  \\:.      \\ \\\\:\\/___/\\                                  \n"
                + "    \\::\\ \\    _\\::\\ \\__\\:.\\-/\\  \\ \\\\::___\\/_                                 \n"
                + "     \\::\\ \\  /__\\::\\__/\\\\. \\  \\  \\ \\\\:\\____/\\                                \n"
                + "      \\__\\/  \\________\\/ \\__\\/ \\__\\/ \\_____\\/ ";
        io.print(waste);
    }

    public void displayOrders(List<Order> orders, LocalDate date) {
        displayBanner(" Orders on " + date.format(DateTimeFormatter.ISO_DATE) + " ");
        orders.stream().forEach(order -> {
            printOrder(order, true);
            displayLine("-");
        });
    }

    /**
     * Summary of the order
     *
     * @param o order to stringify
     * @return
     */
    private void printOrder(Order o, boolean showOrderNum) {
        if (showOrderNum) {
            displayLine("Order #:", " ", Integer.toString(o.getOrderNumber()));
        }
        displayLine("Customer:", " ", o.getCustomerName());
        displayLine("State:", " ", o.getState());
        displayLine("Tax Rate:", " ", o.getTaxRate().toString() + "%");
        displayLine("Product Type:", " ", o.getProductType());
        displayLine("Area (ft^2):", " ", o.getArea().toString());
        displayLine("Cost/ft^2:", " ", "$" + o.getCostPerSquareFoot());
        displayLine("Labor Cost/ft^2:", " ", "$" + o.getLaborCostPerSquareFoot().toString());
        displayLine("Material Cost:", " ", "$" + o.getMaterialCost().toString());
        displayLine("Labor Cost:", " ", "$" + o.getLaborCost().toString());
        displayLine("Total Tax:", " ", "$" + o.getTax().toString());
        displayLine("Total Cost:", " ", "$" + o.getTotal().toString());
    }

    /**
     * display w/out order num
     *
     * @param order
     * @param date
     */
    public void displayOrderWithoutOrderNumber(Order order, LocalDate date) {
        displayLine("Order date:", " ", date.format(DateTimeFormatter.ISO_DATE));
        printOrder(order, false);
    }

    public void displayOrder(Order order, LocalDate date) {
        displayLine("Order date:", " ", date.format(DateTimeFormatter.ISO_DATE));
        printOrder(order, true);
    }

    /**
     * Asks the user to confirm previous action
     *
     * @return
     */
    public boolean confirmAction(String prompt) {
        String decision = null;
        do {
            decision = io.readString(prompt + " (y/n)?");
        } while (!decision.equalsIgnoreCase("y") && !decision.equalsIgnoreCase("yes")
                && !decision.equalsIgnoreCase("n") && !decision.equalsIgnoreCase("no"));

        return decision.equalsIgnoreCase("y") || decision.equalsIgnoreCase("yes");
    }

    public void displaySuccessfulExport() {
        displayLine("=");
        io.print("Successful export.");
        displayLine("=");
    }

    public void displayNoSuchOrder() {
        displayLine("=");
        io.print("No such order found.");
        displayLine("=");
    }

    /**
     * Displays farewell message
     */
    public void displayBye() {
        io.print("_____/\\\\\\\\\\\\\\\\\\\\\\\\_______/\\\\\\\\\\____________/\\\\\\\\\\_______/\\\\\\\\\\\\\\\\\\\\\\\\_____/\\\\\\\\\\\\\\\\\\\\\\\\\\____/\\\\\\________/\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_____/\\\\\\____        \n" +
" ___/\\\\\\//////////______/\\\\\\///\\\\\\________/\\\\\\///\\\\\\____\\/\\\\\\////////\\\\\\__\\/\\\\\\/////////\\\\\\_\\///\\\\\\____/\\\\\\/__\\/\\\\\\///////////____/\\\\\\\\\\\\\\__       \n" +
"  __/\\\\\\_______________/\\\\\\/__\\///\\\\\\____/\\\\\\/__\\///\\\\\\__\\/\\\\\\______\\//\\\\\\_\\/\\\\\\_______\\/\\\\\\___\\///\\\\\\/\\\\\\/____\\/\\\\\\______________/\\\\\\\\\\\\\\\\\\_      \n" +
"   _\\/\\\\\\____/\\\\\\\\\\\\\\__/\\\\\\______\\//\\\\\\__/\\\\\\______\\//\\\\\\_\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\______\\///\\\\\\/______\\/\\\\\\\\\\\\\\\\\\\\\\_____\\//\\\\\\\\\\\\\\__     \n" +
"    _\\/\\\\\\___\\/////\\\\\\_\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\/////////\\\\\\_______\\/\\\\\\_______\\/\\\\\\///////_______\\//\\\\\\\\\\___    \n" +
"     _\\/\\\\\\_______\\/\\\\\\_\\//\\\\\\______/\\\\\\__\\//\\\\\\______/\\\\\\__\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_______\\/\\\\\\_______\\/\\\\\\_______\\/\\\\\\_______________\\//\\\\\\____   \n" +
"      _\\/\\\\\\_______\\/\\\\\\__\\///\\\\\\__/\\\\\\_____\\///\\\\\\__/\\\\\\____\\/\\\\\\_______/\\\\\\__\\/\\\\\\_______\\/\\\\\\_______\\/\\\\\\_______\\/\\\\\\________________\\///_____  \n" +
"       _\\//\\\\\\\\\\\\\\\\\\\\\\\\/_____\\///\\\\\\\\\\/________\\///\\\\\\\\\\/_____\\/\\\\\\\\\\\\\\\\\\\\\\\\/___\\/\\\\\\\\\\\\\\\\\\\\\\\\\\/________\\/\\\\\\_______\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_____/\\\\\\____ \n" +
"        __\\////////////_________\\/////____________\\/////_______\\////////////_____\\/////////////__________\\///________\\///////////////_____\\///_____");
    }

    /**
     * Displays unknown command prompt
     */
    public void displayUnknownCommand() {
        io.print("Unknown Command, try again.");
    }

    /**
     * Line for formatting
     */
    public void displayLine() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public void displayLine(String character) {
        io.print(genLine(character));
    }

    private String genLine(String character) {
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < BANNER_WIDTH; i++) {
            build.append(character.charAt(0));
        }
        return build.toString();
    }

    /**
     * Displays a banner with a certain width, centered.
     *
     * @param message message to display
     */
    public void displayBanner(String message) {
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < (BANNER_WIDTH - message.length()) / 2; i++) {
            build.append(BANNER_CHAR);
        }
        build.append(message);
        for (int i = 0; i < (BANNER_WIDTH - message.length()) / 2; i++) {
            build.append(BANNER_CHAR);
        }
        if (message.length() % 2 == 0) {
            build.append(BANNER_CHAR);
        }
        io.print(build.toString());
    }

    public void displayLine(String left, String middle, String right) {
        StringBuilder build = new StringBuilder(left);
        for (int i = 0; i < BANNER_WIDTH - left.length() - right.length(); i++) {
            build.append(middle.charAt(0));
        }
        build.append(right);
        io.print(build.toString());
    }
}
