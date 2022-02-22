/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.flooringmastery.service;

/**
 *
 * @author larik
 */
public class FlooringMasteryInvalidCustomerNameException extends Exception {

    public FlooringMasteryInvalidCustomerNameException(String message) {
        super(message);
    }

    public FlooringMasteryInvalidCustomerNameException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
