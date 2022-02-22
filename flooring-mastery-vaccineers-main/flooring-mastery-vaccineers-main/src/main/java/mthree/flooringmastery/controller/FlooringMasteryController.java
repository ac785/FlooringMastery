/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;
import mthree.flooringmastery.service.FlooringMasteryInvalidCustomerNameException;
import mthree.flooringmastery.service.FlooringMasteryInvalidAreaException;
import mthree.flooringmastery.service.FlooringMasteryInvalidDateException;
import mthree.flooringmastery.service.FlooringMasteryInvalidOrderNumberException;
import mthree.flooringmastery.service.FlooringMasteryProductNotFoundException;
import mthree.flooringmastery.service.FlooringMasteryServiceLayer;
import mthree.flooringmastery.service.FlooringMasteryStateNotFoundException;
import mthree.flooringmastery.ui.FlooringMasteryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller class. 
 * Calls view and service layer methods to fulfill project requirements.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
@Component
public class FlooringMasteryController {
    /** View layer */
    private FlooringMasteryView view;
    /** Service layer */
    private FlooringMasteryServiceLayer service;

    /**
     * Dependency injection constructor.
     * @param view      view layer
     * @param service   service layer
     */
    @Autowired
    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer service) {
        this.view = view;
        this.service = service;
    }
    
    /**
     * Run method called in main method. 
     * Prompts options, delegates to helper methods, and catches any errors
     */
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        
        try {
            
            while(keepGoing) {
                try {
                    menuSelection = getMenuSelection();

                    switch(menuSelection) {
                        case 1:
                            displayOrders();
                            break;
                        case 2:
                            addAnOrder();
                            break;
                        case 3:
                            editAnOrder();
                            break;
                        case 4:
                            removeAnOrder();
                            break;
                        case 5:
                            exportAllData();
                            break;
                        case 6:
                            keepGoing = false;
                            break;
                        default:
                            showUnknownCommand();
                    }
                } catch (FlooringMasteryStateNotFoundException 
                        | FlooringMasteryProductNotFoundException 
                        | FlooringMasteryInvalidDateException 
                        | FlooringMasteryInvalidOrderNumberException
                        | FlooringMasteryInvalidCustomerNameException
                        | FlooringMasteryInvalidAreaException e) {
                    view.displayLine("=");
                    view.displayErrorMessage(e.getMessage());
                    view.displayLine("=");
                }
            }
            showBye();
        } catch(FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
        
    }
    
    /**
     * Grabs an option from the user
     * @return  an option
     */
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    /**
     * Display order to the user
     */
    private void displayOrders() throws FlooringMasteryPersistenceException, 
            FlooringMasteryInvalidDateException {
        view.displayBanner(" DISPLAY ORDER ");
        LocalDate date = view.getDate("Enter order date");
        Map<Integer, Order> ordersMap = service.getAllOrders(date);
        List<Order> orders = new ArrayList<>(ordersMap.values());
        view.displayOrders(orders, date);
    }
    
    /**
     * Adds an order.
     * First prompts to add minimum info, then displays the summary of the order.
     * Asks for confirmation and creates the order. If any errors occur, this 
     * method is exited.
     * 
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryStateNotFoundException
     * @throws FlooringMasteryProductNotFoundException
     * @throws FlooringMasteryInvalidDateException 
     */
    private void addAnOrder() throws FlooringMasteryPersistenceException, 
            FlooringMasteryStateNotFoundException, 
            FlooringMasteryProductNotFoundException, 
            FlooringMasteryInvalidDateException,
            FlooringMasteryInvalidCustomerNameException,
            FlooringMasteryInvalidAreaException {
        //retrieve states and products
        List<State> states = new ArrayList<>(service.getAllStates().values());
        List<Product> products = new ArrayList<>(service.getAllProducts().values());
        view.displayBanner(" ADD ORDER ");
        //ask user for date
        LocalDate d = view.getFutureDate("Enter future order date");
        
        //ask user for order stuff
        Order o = view.addOrder(states, products);
        
        //send to create order, get full order
        o = service.createOrder(o);
        view.displayOrderSummary();
        view.displayOrderWithoutOrderNumber(o, d);
        view.displayLine("-");
        
        //ask user to confirm yes or no
        //if yes, send to add order
        if(view.confirmAction("Confirm create")) {
            o = service.addOrder(d, o);
            view.displayOrderNumber(o.getOrderNumber());
        } else {
            view.thanksForWastingMyTime();
        }
    }
    
    /**
     * This method has the view prompt the user for the order number and the order
     * date for the order to edit. It then prompts the user for the fields to update.
     * It creates a new order object to pass to the service to update the order mentioned.
     * @throws FlooringMasteryPersistenceException 
     */
    private void editAnOrder() throws FlooringMasteryPersistenceException, 
            FlooringMasteryProductNotFoundException, 
            FlooringMasteryStateNotFoundException, 
            FlooringMasteryInvalidDateException, 
            FlooringMasteryInvalidOrderNumberException,
            FlooringMasteryInvalidCustomerNameException,
            FlooringMasteryInvalidAreaException {
        int orderNumber;
        LocalDate date;
        Order editingOrder; 
        Order newOrder;
        
        view.displayBanner(" EDIT ");
        date = view.getDate("Enter date");                                              // Getting date for order to edit
        orderNumber = view.getOrderNumber();                                            // Getting the order number for order to edit
        editingOrder = service.getOrder(date, orderNumber);                             // Getting order to edit
        view.displayBanner(" ORDER FOUND ");
        view.displayOrder(editingOrder, date);                                          // Displaying order
        view.displayBanner(" EDIT ORDER FOUND ");
        List<State> states = new ArrayList<>(service.getAllStates().values());
        List<Product> products = new ArrayList<>(service.getAllProducts().values());
        newOrder = view.getEditOrder(editingOrder, states, products);                                         // Getting info to update order
        
        if(view.confirmAction("Confirm edit")) {
            service.editOrder(date, orderNumber, newOrder);
            editingOrder = service.getOrder(date, orderNumber);

            view.displayBanner(" UPDATED ORDER ");
            view.displayOrder(editingOrder, date);
        } else {
            view.thanksForWastingMyTime();
        }
        view.displayLine("-");
    }
    
    /**
     * Allow user to remove an order.
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidOrderNumberException
     * @throws FlooringMasteryInvalidDateException 
     */
    private void removeAnOrder() throws FlooringMasteryPersistenceException, 
            FlooringMasteryInvalidOrderNumberException, 
            FlooringMasteryInvalidDateException {
        view.displayBanner(" REMOVE ");
        LocalDate date = view.getDate("Enter order date");
        int orderNumber = view.getOrderNumber();
        Order specificOrder = service.getOrder(date, orderNumber);
        
        if(specificOrder == null) {
            view.displayNoSuchOrder();
        } else if(view.confirmAction("Confirm delete")) { //confirm for deletion
            specificOrder = service.removeOrder(date, orderNumber);
            
            //display removed order if exists
            view.displayBanner(" REMOVED ");
            view.displayOrder(specificOrder, date);
            view.displayLine("-");
        } else {
            view.thanksForWastingMyTime();
        }
        
        
    }
    
    /**
     * Helper method to export data.
     * @throws FlooringMasteryPersistenceException 
     */
    private void exportAllData() throws FlooringMasteryPersistenceException {
        service.exportData();
        view.displaySuccessfulExport();
    }
    
    /** Displays goodbye */
    private void showBye() {
        view.displayBye();
    }
    
    /** Displays if unknown command is provided */
    private void showUnknownCommand() {
        view.displayUnknownCommand();
    }
    
}
