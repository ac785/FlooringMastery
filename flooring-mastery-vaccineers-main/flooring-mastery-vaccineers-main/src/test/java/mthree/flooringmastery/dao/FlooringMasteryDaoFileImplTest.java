/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.dao;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.State;
import mthree.flooringmastery.dto.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Dao Test File. Tests each main dao method.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public class FlooringMasteryDaoFileImplTest {
    
    private FlooringMasteryDao testDao;
    private final String ORDER_PATH;
    private final String PRODUCTS_PATH;
    private final String TAXES_PATH;
    private final String EXPORT_PATH;
    
    public FlooringMasteryDaoFileImplTest () {
        ORDER_PATH = "Test/Orders";
        PRODUCTS_PATH ="Test/Data/Products.txt";
        TAXES_PATH = "Test/Data/Taxes.txt";
        EXPORT_PATH = "Test/Backup/DataExport.txt";
        
        testDao = new FlooringMasteryDaoFileImpl(ORDER_PATH, 
                PRODUCTS_PATH, 
                TAXES_PATH, 
                EXPORT_PATH);
    }
    
    @AfterEach
    public void tearDown() {
        File folder = new File(ORDER_PATH);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            file.delete();
        }
    }

    @Test
    public void testAddGetOrder() throws FlooringMasteryPersistenceException {
        Order firstOrder = new Order();
        
        firstOrder.setOrderNumber(5);
        firstOrder.setCustomerName("Joe Ma");
        firstOrder.setState("KY");
        firstOrder.setTaxRate(BigDecimal.ONE);
        firstOrder.setProductType("Tile");
        firstOrder.setMaterialCost(BigDecimal.ONE);
        firstOrder.setLaborCost(BigDecimal.ONE);
        firstOrder.setTax(BigDecimal.ONE);
        firstOrder.setTotal(BigDecimal.ONE);
        firstOrder.setArea(BigDecimal.ONE);
        firstOrder.setCostPerSquareFoot(BigDecimal.ONE);
        firstOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
        
        
        Order secondOrder = new Order();  

        secondOrder.setOrderNumber(6);
        secondOrder.setCustomerName("Mia K");
        secondOrder.setState("CA");
        secondOrder.setTaxRate(BigDecimal.ONE);
        secondOrder.setProductType("Wood");
        secondOrder.setMaterialCost(BigDecimal.ONE);
        secondOrder.setLaborCost(BigDecimal.ONE);
        secondOrder.setTax(BigDecimal.ONE);
        secondOrder.setTotal(BigDecimal.ONE);
        secondOrder.setArea(BigDecimal.ONE);
        secondOrder.setCostPerSquareFoot(BigDecimal.ONE);
        secondOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
        
        
        Order one = testDao.addOrder(LocalDate.now(), firstOrder);
        Order two = testDao.addOrder(LocalDate.now(), secondOrder);
        
        assertNotNull(one, "Should not be null");
        assertNotNull(two, "Should not be null");
        
        assertEquals(one, firstOrder, "Should be equal since its the same object");
        assertEquals(two, secondOrder, "Should be equal since its the same object");
        
        Order getOne = testDao.getOrder(LocalDate.now(), 1);
        Order getTwo = testDao.getOrder(LocalDate.now(), 2);
        
        assertNotNull(getOne, "Should not be null");
        assertNotNull(getTwo, "Should not be null");
        
        assertEquals(getOne, firstOrder, "First order should equal the order stored previously");
        assertEquals(getTwo, secondOrder, "Second order should equal the order stored previously");
    }
    
    @Test
    public void testGetAllOrders() throws FlooringMasteryPersistenceException {
        Order firstOrder = new Order();
        
        firstOrder.setOrderNumber(5);
        firstOrder.setCustomerName("Joe Ma");
        firstOrder.setState("KY");
        firstOrder.setTaxRate(BigDecimal.ONE);
        firstOrder.setProductType("Tile");
        firstOrder.setMaterialCost(BigDecimal.ONE);
        firstOrder.setLaborCost(BigDecimal.ONE);
        firstOrder.setTax(BigDecimal.ONE);
        firstOrder.setTotal(BigDecimal.ONE);
        firstOrder.setArea(BigDecimal.ONE);
        firstOrder.setCostPerSquareFoot(BigDecimal.ONE);
        firstOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
                 
        Order secondOrder = new Order();  

        secondOrder.setOrderNumber(6);
        secondOrder.setCustomerName("Mia K");
        secondOrder.setState("CA");
        secondOrder.setTaxRate(BigDecimal.ONE);
        secondOrder.setProductType("Wood");
        secondOrder.setMaterialCost(BigDecimal.ONE);
        secondOrder.setLaborCost(BigDecimal.ONE);
        secondOrder.setTax(BigDecimal.ONE);
        secondOrder.setTotal(BigDecimal.ONE);
        secondOrder.setArea(BigDecimal.ONE);
        secondOrder.setCostPerSquareFoot(BigDecimal.ONE);
        secondOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
        
        Map<Integer, Order> orders = testDao.getAllOrders(LocalDate.now());
        assertNull(orders, "No orders should be in the map");
        
        Order a = testDao.addOrder(LocalDate.now(), firstOrder);
        Order b = testDao.addOrder(LocalDate.now(), secondOrder);
        
        orders = testDao.getAllOrders(LocalDate.now());
        assertEquals(2, orders.size(), "Should have the two recent orders");
        
        testDao.removeOrder(LocalDate.now(), a.getOrderNumber());
        orders = testDao.getAllOrders(LocalDate.now());
        assertEquals(1, orders.size(), "Should have 1 order");
        
        testDao.removeOrder(LocalDate.now(), b.getOrderNumber());
        orders = testDao.getAllOrders(LocalDate.now());
        assertEquals(0, orders.size(), "Should be empty");
        
        orders = testDao.getAllOrders(LocalDate.EPOCH);
        assertNull(orders, "Should be null");
    }
    
    @Test
    public void testEditOrder() throws FlooringMasteryPersistenceException {
        LocalDate testDate = LocalDate.now();
        int firstOrderNumber;
        
        Order firstOrder = new Order();
        
        firstOrder.setCustomerName("Joe Ma");
        firstOrder.setState("KY");
        firstOrder.setTaxRate(BigDecimal.ONE);
        firstOrder.setProductType("Tile");
        firstOrder.setMaterialCost(BigDecimal.ONE);
        firstOrder.setLaborCost(BigDecimal.ONE);
        firstOrder.setTax(BigDecimal.ONE);
        firstOrder.setTotal(BigDecimal.ONE);
        firstOrder.setArea(BigDecimal.ONE);
        firstOrder.setCostPerSquareFoot(BigDecimal.ONE);
        firstOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
                 
        Order secondOrder = new Order();  

        secondOrder.setCustomerName("Mia K");
        secondOrder.setState("CA");
        secondOrder.setTaxRate(BigDecimal.ONE);
        secondOrder.setProductType("Wood");
        secondOrder.setMaterialCost(BigDecimal.ONE);
        secondOrder.setLaborCost(BigDecimal.ONE);
        secondOrder.setTax(BigDecimal.ONE);
        secondOrder.setTotal(BigDecimal.ONE);
        secondOrder.setArea(BigDecimal.ONE);
        secondOrder.setCostPerSquareFoot(BigDecimal.ONE);
        secondOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
                
        testDao.addOrder(testDate, firstOrder);
        firstOrderNumber = firstOrder.getOrderNumber();
        secondOrder.setOrderNumber(firstOrderNumber);
        
        Order oldOrder = testDao.editOrder(testDate, firstOrderNumber, secondOrder);
        assertEquals(firstOrder, oldOrder, "Expected first order to equal replaced order.");
      
        Order tempOrder = testDao.getOrder(testDate, firstOrderNumber);
        assertEquals(secondOrder, tempOrder, "Expected second order to equal edited first order.");       
    }
    
    @Test
    public void testRemoveOrder() throws FlooringMasteryPersistenceException{
        
        Order firstOrder = new Order();
        
        firstOrder.setOrderNumber(5);
        firstOrder.setCustomerName("Joe Ma");
        firstOrder.setState("KY");
        firstOrder.setTaxRate(BigDecimal.ONE);
        firstOrder.setProductType("Tile");
        firstOrder.setMaterialCost(BigDecimal.ONE);
        firstOrder.setLaborCost(BigDecimal.ONE);
        firstOrder.setTax(BigDecimal.ONE);
        firstOrder.setTotal(BigDecimal.ONE);
        firstOrder.setArea(BigDecimal.ONE);
        firstOrder.setCostPerSquareFoot(BigDecimal.ONE);
        firstOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
                 
        Order secondOrder = new Order();  

        secondOrder.setOrderNumber(6);
        secondOrder.setCustomerName("Mia K");
        secondOrder.setState("CA");
        secondOrder.setTaxRate(BigDecimal.ONE);
        secondOrder.setProductType("Wood");
        secondOrder.setMaterialCost(BigDecimal.ONE);
        secondOrder.setLaborCost(BigDecimal.ONE);
        secondOrder.setTax(BigDecimal.ONE);
        secondOrder.setTotal(BigDecimal.ONE);
        secondOrder.setArea(BigDecimal.ONE);
        secondOrder.setCostPerSquareFoot(BigDecimal.ONE);
        secondOrder.setLaborCostPerSquareFoot(BigDecimal.ONE);
                
        testDao.addOrder(LocalDate.now(), firstOrder);
        testDao.addOrder(LocalDate.now(), secondOrder);
        
        Order removeOrder = testDao.removeOrder(LocalDate.now(), firstOrder.getOrderNumber());
        
        Map<Integer, Order> allOrders = testDao.getAllOrders(LocalDate.now());
        
        assertNotNull(allOrders, "Orders should not be null.");
        assertEquals(1, allOrders.size(), "There should be atleast 1 order.");
        
        removeOrder = testDao.removeOrder(LocalDate.now(), secondOrder.getOrderNumber());
        assertEquals(removeOrder, secondOrder, "The removed order should be Mia.");
        
        allOrders = testDao.getAllOrders(LocalDate.now());
        
        assertTrue( allOrders.isEmpty(), "The map of orders should be empty");
        
        Order retrievedOrder = testDao.getOrder(LocalDate.now(), firstOrder.getOrderNumber());
        assertNull(retrievedOrder, "Joe was remvoed, should be null.");
        retrievedOrder = testDao.getOrder(LocalDate.now(), secondOrder.getOrderNumber());
        assertNull(retrievedOrder, "Mia was remvoed, should be null.");
    }
    
//    @Test
//    public void testExport() {
//        fail("The test case is a prototype.");
//    }
    
    @Test
    public void testGetAllProducts() throws FlooringMasteryPersistenceException {
        Product carpet = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
        Product laminate = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
        Product tile = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        Product wood = new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75"));
        Map<String, Product> products = testDao.getAllProducts();
        
        assertTrue(products.containsValue(carpet), "Should have carpet");
        assertTrue(products.containsValue(laminate), "Should have laminate");
        assertTrue(products.containsValue(tile), "Should have tile");
        assertTrue(products.containsValue(wood), "Should have wood");
    }
    
    @Test
    public void testGetAllStates() throws FlooringMasteryPersistenceException {
        State california = new State("CA", "California", new BigDecimal("25.00"));
        State kentucky = new State("KY", "Kentucky", new BigDecimal("6.00"));
        State washington = new State("WA", "Washington", new BigDecimal("9.25"));
        State texas = new State("TX", "Texas", new BigDecimal("4.45"));
        Map<String, State> states = testDao.getAllStates(); 
        
        assertTrue(states.containsValue(california) , "Should have California");
        assertTrue(states.containsValue(kentucky) , "Should have Kentucky");
        assertTrue(states.containsValue(washington) , "Should have Washington");
        assertTrue(states.containsValue(texas) , "Should have Texas");
    }
}
