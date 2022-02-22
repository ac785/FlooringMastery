/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.service;

import java.time.LocalDate;
import java.util.Map;
import mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;

/**
 * Service layer interface. 
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public interface FlooringMasteryServiceLayer {

    /**
     * Adds an order to appropriate file.
     * @param date      date of the order
     * @param order     complete order object
     * @return          the added Order with populated order number
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidDateException
     * @throws FlooringMasteryStateNotFoundException
     * @throws FlooringMasteryProductNotFoundException 
     */
    public Order addOrder(LocalDate date, Order order)
            throws FlooringMasteryPersistenceException, 
            FlooringMasteryInvalidDateException, 
            FlooringMasteryStateNotFoundException, 
            FlooringMasteryProductNotFoundException,
            FlooringMasteryInvalidCustomerNameException,
            FlooringMasteryInvalidAreaException;

    /**
     * Creates an order with the minumum information provided.
     * This does not save to anywhere, only constructs the full objecti without
     * the order number.
     * @param order order with minimum data
     * @return  built order with all calculations
     * @throws FlooringMasteryStateNotFoundException
     * @throws FlooringMasteryProductNotFoundException
     * @throws FlooringMasteryPersistenceException 
     */
    public Order createOrder(Order order)
            throws FlooringMasteryStateNotFoundException,
            FlooringMasteryProductNotFoundException,
            FlooringMasteryPersistenceException,
            FlooringMasteryInvalidCustomerNameException,
            FlooringMasteryInvalidAreaException;

    /**
     * Gets a single order
     * @param date          date of order
     * @param orderNumber   order number
     * @return              order associated with date and number
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidDateException
     * @throws FlooringMasteryInvalidOrderNumberException 
     */
    public Order getOrder(LocalDate date, int orderNumber)
            throws FlooringMasteryPersistenceException,
            FlooringMasteryInvalidDateException,
            FlooringMasteryInvalidOrderNumberException;

    /**
     * Returns all orders for given date
     * @param date  order date
     * @return      map of order num --> order object
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidDateException 
     */
    public Map<Integer, Order> getAllOrders(LocalDate date)
            throws FlooringMasteryPersistenceException,
            FlooringMasteryInvalidDateException;

    /**
     * This method will receive new order info in an Order object.This method will
 get the order that needs to be updated and update it with the new order and
 pass it to the DAO to save it.
     * @param date
     * @param orderNumber
     * @param newOrder
     * @return
     * @throws FlooringMasteryPersistenceException
     * @throws mthree.flooringmastery.service.FlooringMasteryStateNotFoundException
     * @throws mthree.flooringmastery.service.FlooringMasteryProductNotFoundException
     * @throws mthree.flooringmastery.service.FlooringMasteryInvalidOrderNumberException
     * @throws mthree.flooringmastery.service.FlooringMasteryInvalidDateException
     */
    public Order editOrder(LocalDate date, int orderNumber, Order newOrder)
            throws FlooringMasteryPersistenceException, 
            FlooringMasteryProductNotFoundException,
            FlooringMasteryStateNotFoundException,
            FlooringMasteryInvalidDateException, 
            FlooringMasteryInvalidOrderNumberException,
            FlooringMasteryInvalidCustomerNameException,
            FlooringMasteryInvalidAreaException;

    /**
     * Removes a specified order.
     * @param date          order date
     * @param orderNumber   order number
     * @return  the order which is to be removed
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidOrderNumberException
     * @throws FlooringMasteryInvalidDateException 
     */
    public Order removeOrder(LocalDate date, int orderNumber)
            throws FlooringMasteryPersistenceException, 
            FlooringMasteryInvalidOrderNumberException, 
            FlooringMasteryInvalidDateException;

    /**
     * Exports data to Backup/DataExport.txt
     * @throws FlooringMasteryPersistenceException 
     */
    public void exportData()
            throws FlooringMasteryPersistenceException;

    /**
     * Gets all products
     * @return  Map of products. Name of product (key) and product dto (value)
     * @throws FlooringMasteryPersistenceException 
     */
    public Map<String, Product> getAllProducts()
            throws FlooringMasteryPersistenceException;

    /**
     * Gets all states
     * @return  Map of state name to state object
     * @throws FlooringMasteryPersistenceException 
     */
    public Map<String, State> getAllStates()
            throws FlooringMasteryPersistenceException;}
