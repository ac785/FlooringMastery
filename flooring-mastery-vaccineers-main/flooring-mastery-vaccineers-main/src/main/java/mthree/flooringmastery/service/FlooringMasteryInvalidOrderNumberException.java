/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.service;

/**
 * Invalid Date Exception. Gets thrown if a order number is not found.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public class FlooringMasteryInvalidOrderNumberException extends Exception {

    public FlooringMasteryInvalidOrderNumberException(String message) {
        super(message);
    }

    public FlooringMasteryInvalidOrderNumberException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
