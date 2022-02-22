/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import mthree.flooringmastery.dao.FlooringMasteryDao;
import mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Dao stub for service layer testing.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
@Component
public class FlooringMasteryDaoFileImplTestStub implements FlooringMasteryDao{

    private Order onlyOrder = new Order();
    private Product onlyProduct = new Product("Test Product", BigDecimal.ONE, BigDecimal.ONE);
    private State onlyState = new State("TX", "Texas", BigDecimal.ONE);
    
    @Autowired
    public FlooringMasteryDaoFileImplTestStub() {
        onlyOrder.setOrderNumber(1);
        onlyOrder.setCustomerName("Test Subject");
        onlyOrder.setState("TX");
        onlyOrder.setTaxRate(BigDecimal.ONE);
        onlyOrder.setProductType("Test Product");
        onlyOrder.setArea(BigDecimal.ONE);
        onlyOrder.setCostPerSquareFoot(BigDecimal.ONE);
        onlyOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
        onlyOrder.setMaterialCost(BigDecimal.ONE);
        onlyOrder.setLaborCost(BigDecimal.ONE);
        onlyOrder.setTax(BigDecimal.ONE);
        onlyOrder.setTotal(BigDecimal.ONE);
    }
    
    public FlooringMasteryDaoFileImplTestStub(Order order){
        this.onlyOrder = order;
    }
    

    @Override
    public Order addOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException {        
        return order;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
        return onlyOrder;
    }

    @Override
    public Map<Integer, Order> getAllOrders(LocalDate date) throws FlooringMasteryPersistenceException {
        Map<Integer, Order> orders = new HashMap<>();
        orders.put(onlyOrder.getOrderNumber(), onlyOrder);
        Map<LocalDate, Map<Integer, Order>> allOrders = new HashMap<>();
        allOrders.put(LocalDate.now(), orders);
        return allOrders.get(date);
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order newOrder) throws FlooringMasteryPersistenceException {
        return onlyOrder;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
        return onlyOrder;
    }

    @Override
    public void exportData() throws FlooringMasteryPersistenceException {
        //nothing
    }

    @Override
    public Map<String, Product> getAllProducts() throws FlooringMasteryPersistenceException {
        Map<String, Product> products = new HashMap<>();
        products.put(onlyProduct.getProductType(), onlyProduct);
        return products;
    }

    @Override
    public Map<String, State> getAllStates() throws FlooringMasteryPersistenceException {
        Map<String, State> states = new HashMap<>();
        states.put(onlyState.getStateAbbreviation(), onlyState);
        return states;
    }
    
}
