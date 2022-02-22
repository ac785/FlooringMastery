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
import mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * File for testing the service layer. Tests business rules and catches
 * appropriate exceptions.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public class FlooringMasteryServiceLayerImplTest {

    private FlooringMasteryServiceLayer testService;

    public FlooringMasteryServiceLayerImplTest() {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("mthree.flooringmastery.service");
        appContext.refresh();

        testService = appContext.getBean("flooringMasteryServiceLayerImpl", FlooringMasteryServiceLayerImpl.class);
    }
    
    @Test
    public void testAddOrder() throws FlooringMasteryPersistenceException {
        Order order = new Order();
        order.setOrderNumber(0);
        order.setCustomerName("Test Subject");
        order.setState("TX");
        order.setTaxRate(BigDecimal.ONE);
        order.setProductType("Test Product");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(BigDecimal.ONE);
        order.setLaborCostPerSquareFoot(BigDecimal.ONE);
        order.setMaterialCost(new BigDecimal("100"));
        order.setLaborCost(new BigDecimal("100"));
        order.setTax(new BigDecimal("2"));
        order.setTotal(new BigDecimal("202"));

        Order testOrder = new Order();
        testOrder.setCustomerName("Test Subject");
        testOrder.setArea(new BigDecimal("100"));
        testOrder.setState("TX");
        testOrder.setProductType("Test Product");

        try {
            assertEquals(order, testService.createOrder(testOrder), "Order updated incorrectly");
            assertEquals(order, testService.addOrder(LocalDate.now().plusDays(1), testOrder), "Order updated incorrectly");
        } catch (FlooringMasteryInvalidDateException
                | FlooringMasteryStateNotFoundException
                | FlooringMasteryProductNotFoundException
                | FlooringMasteryInvalidCustomerNameException
                | FlooringMasteryInvalidAreaException ex) {
            fail("Thrown exception when it shouldn't.");
        }

        order.setState("invalid state");
        try {
            testService.createOrder(order);
            fail("Create order method failed to throw FlooringMasteryStateNotFoundException");
        } catch (FlooringMasteryStateNotFoundException ex) {

        } catch (FlooringMasteryProductNotFoundException | FlooringMasteryInvalidCustomerNameException | FlooringMasteryInvalidAreaException ex) {
            fail("Create order method failed to throw FlooringMasteryStateNotFoundException");
        }

        try {
            testService.addOrder(LocalDate.now().plusDays(1), order);
            fail("Add order method failed to throw FlooringMasteryStateNotFoundException");
        } catch (FlooringMasteryStateNotFoundException ex) {

        } catch (FlooringMasteryProductNotFoundException | FlooringMasteryInvalidCustomerNameException | FlooringMasteryInvalidDateException | FlooringMasteryInvalidAreaException ex) {
            fail("Add order method failed to throw FlooringMasteryStateNotFoundException");
        }

        order.setState("TX");
        order.setProductType("Invalid Product");

        try {
            testService.createOrder(order);
            fail("Create order method failed to throw FlooringMasteryProductNotFoundException");
        } catch (FlooringMasteryProductNotFoundException ex) {

        } catch (FlooringMasteryStateNotFoundException | FlooringMasteryInvalidCustomerNameException | FlooringMasteryInvalidAreaException ex) {
            fail("Create order method failed to throw FlooringMasteryProductNotFoundException");
        }

        try {
            testService.addOrder(LocalDate.now().plusDays(1), order);
            fail("Add order method failed to throw FlooringMasteryProductNotFoundException");
        } catch (FlooringMasteryProductNotFoundException ex) {

        } catch (FlooringMasteryStateNotFoundException | FlooringMasteryInvalidCustomerNameException | FlooringMasteryInvalidDateException | FlooringMasteryInvalidAreaException ex) {
            fail("Add order method failed to throw FlooringMasteryProductNotFoundException");
        }

        order.setProductType("Test Product");
        try {
            testService.addOrder(LocalDate.now(), order);
            fail("Add order method failed to throw FlooringMasteryInvalidDateException");
        } catch (FlooringMasteryInvalidDateException ex) {

        } catch (FlooringMasteryStateNotFoundException
                | FlooringMasteryInvalidCustomerNameException
                | FlooringMasteryProductNotFoundException
                | FlooringMasteryInvalidAreaException ex) {
            fail("Add order method failed to throw FlooringMasteryInvalidDateException");
        }

        order.setCustomerName("Ca$h M0n3y");

        try {
            testService.createOrder(order);
            fail("Add order method failed to throw FlooringMasteryInvalidCustomerNameException");
        } catch (FlooringMasteryInvalidCustomerNameException ex) {

        } catch (FlooringMasteryStateNotFoundException
                | FlooringMasteryProductNotFoundException
                | FlooringMasteryInvalidAreaException ex) {
            fail("Add order method failed to throw FlooringMasteryInvalidCustomerNameException");
        }

        try {
            testService.addOrder(LocalDate.now().plusDays(1), order);
            fail("Add order method failed to throw FlooringMasteryInvalidCustomerNameException");
        } catch (FlooringMasteryInvalidCustomerNameException ex) {

        } catch (FlooringMasteryStateNotFoundException | FlooringMasteryProductNotFoundException | FlooringMasteryInvalidDateException | FlooringMasteryInvalidAreaException ex) {
            fail("Add order method failed to throw FlooringMasteryInvalidCustomerNameException");
        }
        order.setCustomerName("Shrek");

        order.setArea(new BigDecimal("99"));
        try {
            testService.createOrder(order);
            fail("Create order method failed to throw FlooringMasteryInvalidAreaException");
        } catch (FlooringMasteryInvalidAreaException ex) {

        } catch (FlooringMasteryStateNotFoundException
                | FlooringMasteryProductNotFoundException
                | FlooringMasteryInvalidCustomerNameException ex) {
            fail("Create order method failed to throw FlooringMasteryInvalidAreaException");
        }

        try {
            testService.addOrder(LocalDate.now().plusDays(1), order);
            fail("Add order method failed to throw FlooringMasteryInvalidAreaException");
        } catch (FlooringMasteryInvalidAreaException ex) {

        } catch (FlooringMasteryStateNotFoundException
                | FlooringMasteryInvalidDateException
                | FlooringMasteryProductNotFoundException
                | FlooringMasteryInvalidCustomerNameException ex) {
            fail("Add order method failed to throw FlooringMasteryInvalidAreaException");
        }
        order.setArea(new BigDecimal("100"));
    }

    @Test
    public void testGetOrder() throws FlooringMasteryPersistenceException {
        Order testOrder = new Order();
        testOrder.setOrderNumber(1);
        testOrder.setCustomerName("Test Subject");
        testOrder.setState("TX");
        testOrder.setTaxRate(BigDecimal.ONE);
        testOrder.setProductType("Test Product");
        testOrder.setArea(BigDecimal.ONE);
        testOrder.setCostPerSquareFoot(BigDecimal.ONE);
        testOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
        testOrder.setMaterialCost(BigDecimal.ONE);
        testOrder.setLaborCost(BigDecimal.ONE);
        testOrder.setTax(BigDecimal.ONE);
        testOrder.setTotal(BigDecimal.ONE);

        //test if get method works when intended
        Order recievedOrder = new Order();
        try {
            recievedOrder = testService.getOrder(LocalDate.now(), 1);
        } catch (FlooringMasteryInvalidOrderNumberException
                | FlooringMasteryInvalidDateException e) {
            fail("service testGetOrder method failed when it shouldn't");
        }
        assertEquals(recievedOrder, testOrder, "service testGetOrder method didn't remove the right order");

        //test if get method fails when given invalid date
        try {
            recievedOrder = testService.getOrder(LocalDate.MAX, 1);
            fail("service testGetOrder method should've failed for an invalid date");
        } catch (FlooringMasteryInvalidOrderNumberException e) {
            fail("service testGetOrder method failed with wrong exception.");
        } catch (FlooringMasteryInvalidDateException e) {
        }

        //test if get method fails when given invalid order number
        try {
            recievedOrder = testService.getOrder(LocalDate.now(), 10);
            fail("service testGetOrder method should've failed for an invalid order number");
        } catch (FlooringMasteryInvalidDateException e) {
            fail("service testGetOrder method failed with wrong exception.");
        } catch (FlooringMasteryInvalidOrderNumberException e) {
        }
    }

    @Test
    public void testGetAllOrders() throws FlooringMasteryPersistenceException {
        Order testOrder = new Order();
        testOrder.setOrderNumber(1);
        testOrder.setCustomerName("Test Subject");
        testOrder.setState("TX");
        testOrder.setTaxRate(BigDecimal.ONE);
        testOrder.setProductType("Test Product");
        testOrder.setArea(BigDecimal.ONE);
        testOrder.setCostPerSquareFoot(BigDecimal.ONE);
        testOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
        testOrder.setMaterialCost(BigDecimal.ONE);
        testOrder.setLaborCost(BigDecimal.ONE);
        testOrder.setTax(BigDecimal.ONE);
        testOrder.setTotal(BigDecimal.ONE);

        Map<Integer, Order> allOrders = new HashMap<>();

        //test if getAllOrders is working when intended
        try {
            allOrders = testService.getAllOrders(LocalDate.now());
        } catch (FlooringMasteryInvalidDateException e) {
            fail("service getAllOrders method failed to retrieve order");
        }
        assertEquals(allOrders.get(1), testOrder, "service getAllOrders return didn't match with test");

        //test if getAllOrders fails when given an invalid date
        try {
            allOrders = testService.getAllOrders(LocalDate.MAX);
            fail("service getAllOrders method should've failed for invalid date");
        } catch (FlooringMasteryInvalidDateException e) {
        }
    }

    @Test
    public void testEditOrder() throws FlooringMasteryPersistenceException {
        Order testOrder = new Order();
        testOrder.setOrderNumber(1);
        testOrder.setCustomerName("Test Subject");
        testOrder.setState("TX");
        testOrder.setTaxRate(BigDecimal.ONE);
        testOrder.setProductType("Test Product");
        testOrder.setArea(BigDecimal.ONE);
        testOrder.setCostPerSquareFoot(BigDecimal.ONE);
        testOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
        testOrder.setMaterialCost(BigDecimal.ONE);
        testOrder.setLaborCost(BigDecimal.ONE);
        testOrder.setTax(BigDecimal.ONE);
        testOrder.setTotal(BigDecimal.ONE);

        Order testNewOrder = new Order();
        testNewOrder.setCustomerName("Shrek");
        testNewOrder.setState("TX");
        testNewOrder.setProductType("Test Product");
        testNewOrder.setArea(new BigDecimal("100"));

        Order testOrderAfterEdit = new Order();
        testOrderAfterEdit.setOrderNumber(1);
        testOrderAfterEdit.setCustomerName("Shrek");
        testOrderAfterEdit.setState("TX");
        testOrderAfterEdit.setTaxRate(BigDecimal.ONE);
        testOrderAfterEdit.setProductType("Test Product");
        testOrderAfterEdit.setArea(new BigDecimal("100"));
        testOrderAfterEdit.setCostPerSquareFoot(BigDecimal.ONE);
        testOrderAfterEdit.setLaborCostPerSquareFoot(BigDecimal.ONE);
        testOrderAfterEdit.setMaterialCost(new BigDecimal("100"));
        testOrderAfterEdit.setLaborCost(new BigDecimal("100"));
        testOrderAfterEdit.setTax(new BigDecimal("2"));
        testOrderAfterEdit.setTotal(new BigDecimal("202"));

        Order editedOrder = new Order();
        //test if edit works when intended
        try {
            editedOrder = testService.editOrder(LocalDate.now(), 1, testNewOrder);
        } catch (FlooringMasteryProductNotFoundException
                | FlooringMasteryStateNotFoundException
                | FlooringMasteryInvalidDateException
                | FlooringMasteryInvalidOrderNumberException
                | FlooringMasteryInvalidCustomerNameException
                | FlooringMasteryInvalidAreaException e) {
            fail("Service editOrder failed when it shouldn't");
        }
        assertEquals(editedOrder, testOrderAfterEdit, "Service editOrder failed to do correct calculations");

        //test if edit fails for invalid date
        try {
            testService.editOrder(LocalDate.MAX, 1, testNewOrder);
        } catch (FlooringMasteryProductNotFoundException
                | FlooringMasteryStateNotFoundException
                | FlooringMasteryInvalidOrderNumberException
                | FlooringMasteryInvalidCustomerNameException
                | FlooringMasteryInvalidAreaException e) {
            fail("Service editOrder failed with wrong exception");
        } catch (FlooringMasteryInvalidDateException e) {
        }

        //test if edit fails for invalid order number
        try {
            testService.editOrder(LocalDate.now(), 3, testNewOrder);
        } catch (FlooringMasteryProductNotFoundException
                | FlooringMasteryStateNotFoundException
                | FlooringMasteryInvalidDateException
                | FlooringMasteryInvalidCustomerNameException
                | FlooringMasteryInvalidAreaException e) {
            fail("Service editOrder failed with wrong exception");
        } catch (FlooringMasteryInvalidOrderNumberException e) {
        }

        //test if edit fails for invalid state
        testNewOrder.setState("Not a real state");
        try {
            testService.editOrder(LocalDate.now(), 1, testNewOrder);
        } catch (FlooringMasteryProductNotFoundException
                | FlooringMasteryInvalidOrderNumberException
                | FlooringMasteryInvalidDateException
                | FlooringMasteryInvalidCustomerNameException
                | FlooringMasteryInvalidAreaException e) {
            fail("Service editOrder failed with wrong exception");
        } catch (FlooringMasteryStateNotFoundException e) {
        }
        testNewOrder.setState("TX");//reset state

        //test if edit fails for invalid product
        testNewOrder.setProductType("Not a real product");
        try {
            testService.editOrder(LocalDate.now(), 1, testNewOrder);
        } catch (FlooringMasteryStateNotFoundException
                | FlooringMasteryInvalidOrderNumberException
                | FlooringMasteryInvalidDateException
                | FlooringMasteryInvalidCustomerNameException
                | FlooringMasteryInvalidAreaException e) {
            fail("Service editOrder failed with wrong exception");
        } catch (FlooringMasteryProductNotFoundException e) {
        }
        testNewOrder.setProductType("Test Product");//reset product

        try {
            testNewOrder.setCustomerName("Ca$h M0ney");
            testService.editOrder(LocalDate.now(), 1, testNewOrder);
        } catch (FlooringMasteryStateNotFoundException
                | FlooringMasteryInvalidOrderNumberException
                | FlooringMasteryInvalidDateException
                | FlooringMasteryProductNotFoundException
                | FlooringMasteryInvalidAreaException e) {
            fail("Service editOrder failed with wrong exception");
        } catch (FlooringMasteryInvalidCustomerNameException ex) {
        }
        testNewOrder.setCustomerName("Shrek");//reset product

        testNewOrder.setArea(new BigDecimal("99"));
        try{
            testService.editOrder(LocalDate.now(), 1, testNewOrder);
        }catch(FlooringMasteryStateNotFoundException |
                FlooringMasteryInvalidOrderNumberException |
                FlooringMasteryInvalidDateException |
                FlooringMasteryProductNotFoundException |
                FlooringMasteryInvalidCustomerNameException e){
            fail("Service editOrder failed with wrong exception");
        }catch(FlooringMasteryInvalidAreaException e){}
        testNewOrder.setArea(new BigDecimal("100"));
    }

    @Test
    public void testRemoveOrder() throws FlooringMasteryPersistenceException {
        Order testOrder = new Order();
        testOrder.setOrderNumber(1);
        testOrder.setCustomerName("Test Subject");
        testOrder.setState("TX");
        testOrder.setTaxRate(BigDecimal.ONE);
        testOrder.setProductType("Test Product");
        testOrder.setArea(BigDecimal.ONE);
        testOrder.setCostPerSquareFoot(BigDecimal.ONE);
        testOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
        testOrder.setMaterialCost(BigDecimal.ONE);
        testOrder.setLaborCost(BigDecimal.ONE);
        testOrder.setTax(BigDecimal.ONE);
        testOrder.setTotal(BigDecimal.ONE);

        //test if remove method works when intended
        Order removedOrder = new Order();
        try {
            removedOrder = testService.removeOrder(LocalDate.now(), 1);
        } catch (FlooringMasteryInvalidOrderNumberException
                | FlooringMasteryInvalidDateException e) {
            fail("service removeOrder method failed when it shouldn't");
        }
        assertEquals(removedOrder, testOrder, "service removeOrder method didn't remove the right order");

        //test if remove method fails when given invalid date
        try {
            removedOrder = testService.removeOrder(LocalDate.MAX, 1);
            fail("service removeOrder method should've failed for an invalid date");
        } catch (FlooringMasteryInvalidOrderNumberException e) {
            fail("service removeOrder method failed with wrong exception.");
        } catch (FlooringMasteryInvalidDateException e) {
        }

        //test if remove method fails when given invalid order number
        try {
            removedOrder = testService.removeOrder(LocalDate.now(), 10);
            fail("service removeOrder method should've failed for an invalid order number");
        } catch (FlooringMasteryInvalidDateException e) {
            fail("service removeOrder method failed with wrong exception.");
        } catch (FlooringMasteryInvalidOrderNumberException e) {
        }
    }

//    @Test
//    public void testExport() {
//        fail("The test case is a prototype.");
//    }
    @Test
    public void testGetAllProducts() throws FlooringMasteryPersistenceException {
        Product testProduct = new Product("Test Product", BigDecimal.ONE, BigDecimal.ONE);

        //test if getAllProducts is working when intended
        Map<String, Product> products = testService.getAllProducts();
        if (products.isEmpty()) {
            fail("service getAllProducts method returned empty map");
        }
        assertEquals(products.get("Test Product"), testProduct, "service getAllProducts return didn't match test");
    }

    @Test
    public void testGetAllStates() throws FlooringMasteryPersistenceException {
        State testState = new State("TX", "Texas", BigDecimal.ONE);

        //test if getAllStates is working when intended
        Map<String, State> states = testService.getAllStates();
        if (states.isEmpty()) {
            fail("service getAllStates method returned empty map");
        }
        assertEquals(states.get("TX"), testState, "service getAllStates return didn't match test");
    }

}
