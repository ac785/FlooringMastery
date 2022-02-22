/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.service;

import mthree.flooringmastery.dao.FlooringMasteryAuditDao;
import mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AuditDdao File Stub for testing.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
@Component
public class FlooringMasteryAuditDaoFileImplTestStub implements FlooringMasteryAuditDao{

    @Autowired
    public FlooringMasteryAuditDaoFileImplTestStub() {
    }
    
    
    @Override
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException {
        //nothing
    }
    
}
