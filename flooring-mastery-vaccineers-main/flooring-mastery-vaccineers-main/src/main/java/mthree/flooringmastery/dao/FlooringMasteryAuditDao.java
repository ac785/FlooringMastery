/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.dao;

/**
 * Controller class. 
 * Calls view and service layer methods to fulfill project requirements.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public interface FlooringMasteryAuditDao {
    
    /**
     * Writes entry to audit log file.
     * @param entry     String to add to file
     * @throws FlooringMasteryPersistenceException 
     */
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException;
}
