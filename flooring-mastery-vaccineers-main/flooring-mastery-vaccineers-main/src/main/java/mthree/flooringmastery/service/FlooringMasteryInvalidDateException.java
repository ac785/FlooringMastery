/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.service;

/**
 * Invalid Date Exception. Gets thrown if a date provided does not match 
 * constraints. Cannot add a date with a past date.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public class FlooringMasteryInvalidDateException extends Exception {

    public FlooringMasteryInvalidDateException(String message) {
        super(message);
    }

    public FlooringMasteryInvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
