/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * File implementation of AuditDao.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
@Component
public class FlooringMasteryAuditDaoFileImpl implements FlooringMasteryAuditDao{
    
    /** Audit file */
    private final String AUDIT_FILE;
    
    @Autowired
    public FlooringMasteryAuditDaoFileImpl() {
        this.AUDIT_FILE = "audit.txt";
    }
    
    public FlooringMasteryAuditDaoFileImpl(String auditTestFile) {
        this.AUDIT_FILE = auditTestFile;
    }
    

    /**
     * Writes entry to audit log file.
     * @param entry     String to add to file
     * @throws FlooringMasteryPersistenceException 
     */
    @Override
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException {
         PrintWriter out;
         try {
             out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
         } catch (IOException e) {
             throw new FlooringMasteryPersistenceException("Could not persist audit information", e);
         }
         LocalDateTime timestamp = LocalDateTime.now();
         out.println(timestamp.toString() + " : " +entry);
         out.flush();
    }
}
