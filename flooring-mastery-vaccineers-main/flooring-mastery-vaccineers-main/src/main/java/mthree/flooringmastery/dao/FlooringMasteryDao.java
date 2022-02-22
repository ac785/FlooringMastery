/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.dao;

import java.time.LocalDate;
import java.util.Map;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;

/**
 * File implementation of AuditDao. 
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public interface FlooringMasteryDao {

    /**
     * Adds an order to appropriate file.
     * @param date      date of the order
     * @param order     complete order object
     * @return          the added Order with populated order number
     * @throws FlooringMasteryPersistenceException 
     */
    public Order addOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException;
    
    /**
     * Get a single order.
     * @param date          Gets a single order from the file
     * @param orderNumber   positive integer
     * @return              order if exists, null if not
     * @throws FlooringMasteryPersistenceException 
     */
    public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;
    
    /**
     * Gets all orders for a provided date.
     * @param date  order date
     * @return  Map of all orders. Order number (key) to Order (value)
     * @throws FlooringMasteryPersistenceException 
     */
    public Map<Integer, Order> getAllOrders(LocalDate date) throws FlooringMasteryPersistenceException;
    
    /**
     * Edits an order and returns the old one.
     * Saves the new order in place of the old one. This does not change the
     * order number.
     * @param date          order date
     * @param orderNumber   order number
     * @param newOrder      Order with updated information
     * @return              Old order
     * @throws FlooringMasteryPersistenceException 
     */
    public Order editOrder(LocalDate date, int orderNumber, Order newOrder) throws FlooringMasteryPersistenceException;
    
    /**
     * Removes a specified order.
     * @param date          order date
     * @param orderNumber   order number
     * @return  the order which is to be removed
     * @throws FlooringMasteryPersistenceException 
     */
    public Order removeOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;
    
    /**
     * Exports data to Backup/DataExport.txt
     * @throws FlooringMasteryPersistenceException 
     */
    public void exportData() throws FlooringMasteryPersistenceException;
    
    /**
     * Gets all products
     * @return  Map of products. Name of product (key) and product dto (value)
     * @throws FlooringMasteryPersistenceException 
     */
    public Map<String, Product> getAllProducts() throws FlooringMasteryPersistenceException;

    /**
     * Gets all states
     * @return  Map of state name to state object
     * @throws FlooringMasteryPersistenceException 
     */
    public Map<String, State> getAllStates() throws FlooringMasteryPersistenceException;

}
