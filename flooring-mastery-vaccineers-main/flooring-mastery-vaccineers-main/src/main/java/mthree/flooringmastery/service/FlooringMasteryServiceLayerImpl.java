/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import mthree.flooringmastery.dao.FlooringMasteryAuditDao;
import mthree.flooringmastery.dao.FlooringMasteryDao;
import mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import mthree.flooringmastery.dto.Order;

import mthree.flooringmastery.dto.Product;

import mthree.flooringmastery.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service layer interface. 
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
@Component
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer{

    private FlooringMasteryDao dao;
    private FlooringMasteryAuditDao auditDao;

    @Autowired
    public FlooringMasteryServiceLayerImpl(FlooringMasteryDao dao, FlooringMasteryAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }

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
    @Override
    public Order addOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidDateException, FlooringMasteryStateNotFoundException, FlooringMasteryProductNotFoundException, FlooringMasteryInvalidCustomerNameException, FlooringMasteryInvalidAreaException {
        if(date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())){
            throw new FlooringMasteryInvalidDateException("ERROR: Order date is not in the future.");
        }
        updateRemainingInfo(order);
        order = dao.addOrder(date, order);
        auditDao.writeAuditEntry("NEW ORDER ADDED | Order Date: " + date.format(DateTimeFormatter.ISO_DATE) + " | " + order.toString());
        return order;
    }

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
    @Override
    public Order createOrder(Order order) throws FlooringMasteryPersistenceException, FlooringMasteryStateNotFoundException, FlooringMasteryProductNotFoundException, FlooringMasteryInvalidCustomerNameException, FlooringMasteryInvalidAreaException  {
        updateRemainingInfo(order);
        return order;
    }

    /**
     * Gets a single order
     * @param date          date of order
     * @param orderNumber   order number
     * @return              order associated with date and number
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidDateException
     * @throws FlooringMasteryInvalidOrderNumberException 
     */
    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidOrderNumberException, FlooringMasteryInvalidDateException{
        Map<Integer, Order> orders = dao.getAllOrders(date);
        if (orders == null) {
            throw new FlooringMasteryInvalidDateException("ERROR: No orders exist for this date.");
        }
        Order order = orders.get(orderNumber);
        if (order == null) {
            throw new FlooringMasteryInvalidOrderNumberException("ERROR: Order number is not valid.");
        }
        return order;
    }

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
    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order newOrder) throws FlooringMasteryPersistenceException, FlooringMasteryStateNotFoundException, FlooringMasteryProductNotFoundException, FlooringMasteryInvalidOrderNumberException, FlooringMasteryInvalidDateException, FlooringMasteryInvalidCustomerNameException, FlooringMasteryInvalidAreaException {
        Order editingOrder  = getOrder(date, orderNumber);
        updateOrder(editingOrder, newOrder);
        updateRemainingInfo(editingOrder);
        Order oldOrder = dao.editOrder(date, orderNumber, editingOrder);
        auditDao.writeAuditEntry("ORDER EDITED | Order Date: " +  date.format(DateTimeFormatter.ISO_DATE) + " | " + editingOrder.toString());
        return oldOrder;
    }
    
    /**
     * Returns all orders for given date
     * @param date  order date
     * @return      map of order num --> order object
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidDateException 
     */
    @Override
    public Map<Integer, Order> getAllOrders(LocalDate date) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidDateException{
        Map<Integer, Order> allOrders = dao.getAllOrders(date);
        if(allOrders == null || allOrders.isEmpty()) {
            throw new FlooringMasteryInvalidDateException("Error, no orders exist for that date.");
        }
        
        return dao.getAllOrders(date);
    }

    /**
     * Removes a specified order.
     * @param date          order date
     * @param orderNumber   order number
     * @return  the order which is to be removed
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidOrderNumberException
     * @throws FlooringMasteryInvalidDateException 
     */
    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidOrderNumberException, FlooringMasteryInvalidDateException {
        getOrder(date, orderNumber);
        Order order = dao.removeOrder(date, orderNumber);
        auditDao.writeAuditEntry("ORDER DELETED | Order Date: " + date.format(DateTimeFormatter.ISO_DATE) + " | " + order.toString());
        
        return order;
    }
    
    /**
     * This method will get the remaining order that has only what the user
     * has sent input for. This method will update the remaining variables that
     * do not require calculations:
     * (costPerSquareFoot, LaborCostPerSquareFoot, taxRate)
     * @param order
     * @throws FlooringMasteryPersistenceException 
     * @throws FlooringMasteryStateNotFoundException 
     * @throws FlooringMasteryProductNotFoundException
     */
    private void updateRemainingInfo(Order order) throws FlooringMasteryPersistenceException, 
            FlooringMasteryStateNotFoundException, 
            FlooringMasteryProductNotFoundException,
            FlooringMasteryInvalidCustomerNameException,
            FlooringMasteryInvalidAreaException{
        State state = dao.getAllStates().get(order.getState());
        if(state == null) {
            throw new FlooringMasteryStateNotFoundException("ERROR: State was not found.");
        }
        
        if(!order.getCustomerName().trim().matches("[a-zA-Z0-9 \\.]+")) {
            throw new FlooringMasteryInvalidCustomerNameException("Name must be numbers, letters, dots, or space");
        }
        
        Product product = dao.getAllProducts().get(order.getProductType());
        if(product == null) {
            throw new FlooringMasteryProductNotFoundException("ERROR: Product was not found.");
        }
        
        if(order.getArea().compareTo(new BigDecimal("100")) < 0){
            throw new FlooringMasteryInvalidAreaException("ERROR: Area must be at least 100ft^2.");
        }
        
        BigDecimal costPerSquareFoot = product.getCostPerSquareFoot();
        BigDecimal laborCostPerSquareFoot = product.getLaborCostPerSquareFoot();
        BigDecimal taxRate = state.getTaxRate();
        
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.setTaxRate(taxRate);
        
        calculateRemainingInfo(order);
    }

    /**
     * This method will get the remaining order that has everything except
     * the calculation portion of the variables filled. This method will
     * calculate and update the remaining variables:
     * (materialCost, laborCost, tax, total)
     * @param order 
     */
    private void calculateRemainingInfo(Order order) {
        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());
        BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot());
        
        BigDecimal tax = order.getTaxRate().divide(new BigDecimal("100")).multiply(materialCost.add(laborCost));
        BigDecimal total = materialCost.add(laborCost.add(tax));
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
    }

    /**
     * This method will take in the order object that is to be updated
     * and update it with the new order information from the new order object.
     * This is mainly to update fields that aren't empty and skip the ones
     * that are.
     * @param editingOrder
     * @param newOrder
     */
    private void updateOrder(Order editingOrder, Order newOrder) {
        String customerName = newOrder.getCustomerName();
        String state = newOrder.getState();
        String productType = newOrder.getProductType();
        BigDecimal area = newOrder.getArea();

        if(!(customerName == null || customerName.trim().isEmpty())) {
            editingOrder.setCustomerName(customerName);
        }

        if(!(state == null || state.trim().isEmpty())) {
            editingOrder.setState(state);
        }

        if(!(productType == null || productType.trim().isEmpty())) {
            editingOrder.setProductType(productType);
        }

        if(area != null) {
            editingOrder.setArea(area);
        }
    }
    
    /**
     * Gets all products
     * @return  Map of products. Name of product (key) and product dto (value)
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public Map<String, Product> getAllProducts() throws FlooringMasteryPersistenceException {
        return dao.getAllProducts();
    }

    /**
     * Exports data to Backup/DataExport.txt
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public void exportData() throws FlooringMasteryPersistenceException{
        dao.exportData();
        auditDao.writeAuditEntry("DATA EXPORTED TO BACKUP");
    }

    /**
     * Gets all states
     * @return  Map of state name to state object
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public Map<String, State> getAllStates() throws FlooringMasteryPersistenceException {
        return dao.getAllStates();

    }
}
