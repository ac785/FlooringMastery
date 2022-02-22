/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import mthree.flooringmastery.dto.Order;
import mthree.flooringmastery.dto.Product;
import mthree.flooringmastery.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * File implementation of the main dao. 
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
@Component
public class FlooringMasteryDaoFileImpl implements FlooringMasteryDao{

    /** Directory of order files */
    private final String ORDER_PATH;
    
    /** Product file name */
    private final String PRODUCT_FILE;
    
    /** Tax file name */
    private final String TAX_FILE;
    
    /** Backup file name */
    private final String BACKUP_FILE;
    
    /** Delimiter when saving orders */
    private static final String DELIMITER = ",";
    
    /** Orders map. Order date --> ( order number --> order object) */
    private Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();
    
    /** Map of products. Product type --> Product */
    private Map<String, Product> products = new HashMap<>();
    
    /** Map of states. State name --> State*/
    private Map<String, State> states = new HashMap<>();
    private int maxOrderNumber = 0;
    
    @Autowired
    public FlooringMasteryDaoFileImpl() {
        this.ORDER_PATH = "Orders";
        this.PRODUCT_FILE = "Data/Products.txt";
        this.TAX_FILE = "Data/Taxes.txt";
        this.BACKUP_FILE = "Backup/DataExport.txt";
        createOrdersFolder();
    }
    
    public FlooringMasteryDaoFileImpl(String orderPath, String productFile, String taxFile, String backupFile) {
        this.ORDER_PATH = orderPath;
        this.PRODUCT_FILE = productFile;
        this.TAX_FILE = taxFile;
        this.BACKUP_FILE  = backupFile;
        createOrdersFolder();
    }
    
    private void createOrdersFolder() {
        File folder = new File(ORDER_PATH);
        if(!folder.exists())
            folder.mkdirs();
    }

    /**
     * Adds an order to appropriate file.
     * @param date      date of the order
     * @param order     complete order object
     * @return          the added Order with populated order number
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public Order addOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException {
        loadOrders();
        maxOrderNumber++;
        order.setOrderNumber(maxOrderNumber);
        Map<Integer, Order> dateOrders = orders.get(date);
        if (dateOrders == null) {
            dateOrders = new HashMap<Integer, Order>();       
        }
        
        dateOrders.put(maxOrderNumber, order);
        orders.put(date, dateOrders);
        saveOrders();
        return order;
    }
    
    /**
     * Get a single order.
     * @param date          Gets a single order from the file
     * @param orderNumber   positive integer
     * @return              order if exists, null if not
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
        Map<Integer, Order> dateOrders = getAllOrders(date);
        Order order = dateOrders.get(orderNumber);
        return order;
    }
    
    /**
     * Gets all orders for a provided date.
     * @param date  order date
     * @return  Map of all orders. Order number (key) to Order (value)
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public Map<Integer, Order> getAllOrders(LocalDate date) throws FlooringMasteryPersistenceException {
        loadOrders();
        Map<Integer, Order> dateOrders = orders.get(date);
        return dateOrders;
    }

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
    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order newOrder) throws FlooringMasteryPersistenceException {
        Map<Integer, Order> dateOrders = getAllOrders(date);
        Order oldOrder = dateOrders.put(orderNumber, newOrder);
        orders.put(date, dateOrders);
        saveOrders();
        return oldOrder;
    }

    /**
     * Removes a specified order.
     * @param date          order date
     * @param orderNumber   order number
     * @return  the order which is to be removed
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
        Map<Integer, Order> dateOrders = getAllOrders(date);
        Order removedOrder = dateOrders.remove(orderNumber);
        orders.put(date, dateOrders);
        saveOrders();
        return removedOrder;
    }
    
    /**
     * Exports data to Backup/DataExport.txt
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public void exportData() throws FlooringMasteryPersistenceException{
        loadOrders();
        PrintWriter out;
        try{
            out = new PrintWriter(new FileWriter(BACKUP_FILE));
        }catch(IOException e){
            throw new FlooringMasteryPersistenceException("Could not backup order data.", e);
        }
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");
        out.flush();
        
        Set<LocalDate> allDates = orders.keySet();
        allDates.stream().forEach(date -> {
            
            Map<Integer, Order> ordersInDate = orders.get(date);
            String orderAsText;
            
            for(Order currentOrder : ordersInDate.values()){
                orderAsText = marshallOrder(currentOrder);
                //adding on date at the end for the backup file
                orderAsText += DELIMITER + date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                out.println(orderAsText);
                out.flush();
            }
        });
        out.close();
    }

    
    /**
     * Gets all products
     * @return  Map of products. Name of product (key) and product dto (value)
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public Map<String, Product> getAllProducts() throws FlooringMasteryPersistenceException {
        loadProducts();
        return products;
    }

    /**
     * Gets all states
     * @return  Map of state name to state object
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public Map<String, State> getAllStates() throws FlooringMasteryPersistenceException {
        loadStates();
        return states;
    }
    
    /**
     * Helper method to unmarshall order
     * @param orderAsText   order given as a line of text from a file
     * @return Complete order object
     */
    private Order unmarshallOrder(String orderAsText){
        String[] orderTokens = orderAsText.split(DELIMITER);

        Order orderFromFile = new Order();
        orderFromFile.setOrderNumber(Integer.parseInt(orderTokens[0]));
        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setState(orderTokens[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]).setScale(2, RoundingMode.HALF_UP));
        orderFromFile.setProductType(orderTokens[4]);
        orderFromFile.setArea(new BigDecimal(orderTokens[5]).setScale(2, RoundingMode.HALF_UP));
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]).setScale(2, RoundingMode.HALF_UP));
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]).setScale(2, RoundingMode.HALF_UP));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]).setScale(2, RoundingMode.HALF_UP));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]).setScale(2, RoundingMode.HALF_UP));
        orderFromFile.setTax(new BigDecimal(orderTokens[10]).setScale(2, RoundingMode.HALF_UP));
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]).setScale(2, RoundingMode.HALF_UP));
        return orderFromFile;
    }

    /**
     * Helper method to unmarshall product
     * @param productAsText product given as a line of text from a file
     * @return complete product
     */
    private Product unmarshallProduct(String productAsText){
        String[] productTokens = productAsText.split(DELIMITER);

        Product productFromFile = new Product(
                productTokens[0],
                new BigDecimal(productTokens[1]).setScale(2, RoundingMode.HALF_UP),
                new BigDecimal(productTokens[2]).setScale(2, RoundingMode.HALF_UP));
        return productFromFile;
    }

    /**
     * Helper method to unmarshall state
     * @param stateAsText   state given as a line of text from a file
     * @return  Complete state
     */
    private State unmarshallState(String stateAsText){
        String[] stateTokens = stateAsText.split(DELIMITER);

        State stateFromFile = new State(
                stateTokens[0],
                stateTokens[1],
                new BigDecimal(stateTokens[2]).setScale(2, RoundingMode.HALF_UP));
        return stateFromFile;
    }

    /**
     * Helper method to load orders from the order file
     * @throws FlooringMasteryPersistenceException 
     */
    private void loadOrders() throws FlooringMasteryPersistenceException{
        
        File folder = new File(ORDER_PATH);
        File[] listOfFiles = folder.listFiles();
        
        for (File file : listOfFiles) {
            Scanner scanner;
            try{
                scanner = new Scanner(new BufferedReader(new FileReader(file)));
            }catch(FileNotFoundException e){
                throw new FlooringMasteryPersistenceException("Could not load order data into memory.", e);
            }

            String dateString = file.getName().substring(7,15); //get the date from the file name
            LocalDate orderDate;
            try{
                orderDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMddyyy"));
            }catch(DateTimeParseException e){
                throw new FlooringMasteryPersistenceException("Order file has invalid date.", e);
            }
            
            Map<Integer, Order> ordersInDate = new HashMap<>();
            String currentLine;
            Order currentOrder;
            scanner.nextLine(); //skip first line since it's not part of the data

            while(scanner.hasNextLine()){
                currentLine = scanner.nextLine();
                currentOrder = unmarshallOrder(currentLine);
                //calculate the max order number for when making new orders
                if(currentOrder.getOrderNumber() > maxOrderNumber){
                    maxOrderNumber = currentOrder.getOrderNumber();
                }
                ordersInDate.put(currentOrder.getOrderNumber(), currentOrder);
            }
            orders.put(orderDate, ordersInDate);
            
            scanner.close();
            if(ordersInDate.isEmpty()) {
                file.delete();
            }
        }
    }

    /**
     * Helper method to load products from product file
     * @throws FlooringMasteryPersistenceException 
     */
    private void loadProducts() throws FlooringMasteryPersistenceException{
        Scanner scanner;

        try{
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        }catch(FileNotFoundException e){
            throw new FlooringMasteryPersistenceException("Could not load product data into memory.", e);
        }

        String currentLine;
        Product currentProduct;
        scanner.nextLine(); //skip first line since it's not part of the data
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(), currentProduct);
        }
        scanner.close();
    }

    /**
     * Helper method to load states from states file
     * @throws FlooringMasteryPersistenceException 
     */
    private void loadStates() throws FlooringMasteryPersistenceException{
        Scanner scanner;

        try{
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        }catch(FileNotFoundException e){
            throw new FlooringMasteryPersistenceException("Could not load tax data into memory.", e);
        }

        String currentLine;
        State currentState;
        scanner.nextLine(); //skip first line since it's not part of the data
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentState = unmarshallState(currentLine);
            states.put(currentState.getStateAbbreviation(), currentState);
        }
        scanner.close();
    }

    /**
     * Helper method to marshall an order
     * @param order Takes an order
     * @return  One line of text with all required info
     */
    private String marshallOrder(Order order){
        String orderAsText = String.valueOf(order.getOrderNumber()) + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getState() + DELIMITER;
        orderAsText += order.getTaxRate().toString() + DELIMITER;
        orderAsText += order.getProductType() + DELIMITER;
        orderAsText += order.getArea().toString() + DELIMITER;
        orderAsText += order.getCostPerSquareFoot().toString() + DELIMITER;
        orderAsText += order.getLaborCostPerSquareFoot().toString() + DELIMITER;
        orderAsText += order.getMaterialCost().toString() + DELIMITER;
        orderAsText += order.getLaborCost().toString() + DELIMITER;
        orderAsText += order.getTax().toString() + DELIMITER;
        orderAsText += order.getTotal().toString();
        
        return orderAsText;
    }

    /**
     * Helper method to save all the orders to order file
     * @throws FlooringMasteryPersistenceException 
     */
    private void saveOrders() throws FlooringMasteryPersistenceException{
        
        Set<LocalDate> allDates = orders.keySet();
        
        for (LocalDate date : allDates) {
            PrintWriter out;
            String dateString = date.format(DateTimeFormatter.ofPattern("MMddyyy"));
            try{
                out = new PrintWriter(new FileWriter(ORDER_PATH + "/Orders_" + dateString + ".txt"));
            }catch(IOException e){
                throw new FlooringMasteryPersistenceException("Could not save order data.", e);
            }
            
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            
            Map<Integer, Order> ordersInDate = orders.get(date);
            String orderAsText;
            
            for(Order currentOrder : ordersInDate.values()){
                orderAsText = marshallOrder(currentOrder);
                out.println(orderAsText);
                out.flush();
            }
            out.close();
        }
    }
}
