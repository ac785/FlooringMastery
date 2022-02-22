/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.service;

/**
 * Product not found. Gets thrown if a product is not found when passed from 
 * view layer.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public class FlooringMasteryProductNotFoundException extends Exception {

    public FlooringMasteryProductNotFoundException(String message) {
        super(message);
    }

    public FlooringMasteryProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
