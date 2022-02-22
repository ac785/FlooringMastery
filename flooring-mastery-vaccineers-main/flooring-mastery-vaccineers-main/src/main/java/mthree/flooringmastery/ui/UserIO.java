/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * UserIO layer. Methods directly asking for user input.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
public interface UserIO {
    void print(String msg);
    
    double readDouble(String prompt);
    double readDouble(String prompt, double min, double max);
    
    float readFloat(String prompt);
    float readFloat(String prompt, float min, float max);
    
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);
    
    long readLong(String prompt);
    long readLong(String prompt, long min, long max);
    
    String readString(String prompt);
    
    LocalDate readDate(String prompt);
    LocalDate readFutureDate(String prompt);
    
    BigDecimal readBigDecimal(String prompt, boolean allowedNull);
}
