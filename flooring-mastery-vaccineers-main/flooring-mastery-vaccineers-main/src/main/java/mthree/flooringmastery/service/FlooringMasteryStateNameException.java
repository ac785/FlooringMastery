/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.service;

/**
 * Invalid State Name. Gets thrown if a state name is not part of the files.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public class FlooringMasteryStateNameException extends Exception {

    public FlooringMasteryStateNameException(String message) {
        super(message);
    }

    public FlooringMasteryStateNameException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
