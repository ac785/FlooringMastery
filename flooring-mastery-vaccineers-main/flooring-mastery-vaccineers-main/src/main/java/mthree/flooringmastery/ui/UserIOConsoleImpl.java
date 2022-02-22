/**
 * Team Vaccineers.
 *
 * Contains the full solution to assessment Flooring Mastery for
 * C166 Full Stack Development with Java and Angular (2201).
 */
package mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 * UserIO Implementation. Methods directly asking for user input.
 * 
 * @author Andy Bae
 * @author Alexi Mellovich
 * @author Adem Coklar
 * @author Illarion Eremenko
 */
@Component
public class UserIOConsoleImpl implements UserIO {
    
    final private Scanner console = new Scanner(System.in);
    
    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public String readString(String prompt) {
        this.print(prompt);
        return console.nextLine();
    }
    
    @Override
    public double readDouble(String prompt) {
        while(true){
            try{
                return Double.parseDouble(this.readString(prompt));
            }catch (NumberFormatException e){
                this.print("Needs to be a Double");
            }
        }
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double result;
        do{
            result = this.readDouble(prompt);
        }while(result < min || result > max);
        
        return result;
    }

    @Override
    public float readFloat(String prompt) {
        while(true){
            try{
                return Float.parseFloat(this.readString(prompt));
            }catch (NumberFormatException e){
                this.print("Needs to be a Float");
            }
        }
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float result;
        do{
            result = this.readFloat(prompt);
        }while(result < min || result > max);
        
        return result;
    }

    @Override
    public int readInt(String prompt) {
        while(true){
            try{
                return Integer.parseInt(this.readString(prompt));
            }catch (NumberFormatException e){
                this.print("Needs to be an Integer");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int result;
        do{
            result = this.readInt(prompt);
        }while(result < min || result > max);
        
        return result;
    }

    @Override
    public long readLong(String prompt) {
        while(true){
            try{
                return Long.parseLong(this.readString(prompt));
            }catch (NumberFormatException e){
                this.print("Needs to be a Long");
            }
        }
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long result;
        do{
            result = this.readLong(prompt);
        }while(result < min || result > max);
        
        return result;
    }
    
    @Override
    public LocalDate readDate(String prompt) {
        LocalDate date = null;
        print(prompt);
        int year = readInt("Year: ", 0, 9999);
        int month = readInt("Month (1-12): ", 1, 12);
        //month starts at 0, only used to get number of days
        Calendar mycal = new GregorianCalendar(year, month-1, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int day = readInt("Day (1-"+ String.valueOf(daysInMonth) + "): ", 1, daysInMonth);
        date = LocalDate.parse(formatDate(year, month, day));
        return date;
    }
    
    /**
     * Formats the provided info into YYYY-MM-DD ISO format
     * @param year
     * @param month
     * @param day
     * @return 
     */
    private String formatDate(int year, int month, int day) {
        DecimalFormat dfYear = new DecimalFormat("0000");
        DecimalFormat dfMonthDay = new DecimalFormat("00");
        return dfYear.format(year) + "-" + dfMonthDay.format(month) + "-" + dfMonthDay.format(day);
    }
    
    @Override
    public LocalDate readFutureDate(String prompt) {
        LocalDate date = null;
        do {
            //if it's already going a second or more time around
            if(date != null) {
                print("Date is not in the future");
            }
            print(prompt);
            int year = readInt("Year: ", LocalDate.now().getYear(), 9999);
            int month = readInt("Month (1-12): ", 1, 12);
            //month starts at 0, only used to get number of days
            Calendar mycal = new GregorianCalendar(year, month-1, 1);
            int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int day = readInt("Day (1-"+ String.valueOf(daysInMonth) + "): ", 1, daysInMonth);
            date = LocalDate.parse(formatDate(year, month, day));
        } while(date.isBefore(LocalDate.now()));
        return date;
    }
    
    /**
     * This method will take in input as a BigDecimal. If the user
     * enters in nothing or white spaces, it will return null. If 
     * the user enters in an invalid number, it will prompt the user
     * again.
     * @param prompt
     * @return 
     */
    @Override
    public BigDecimal readBigDecimal(String prompt, boolean allowedNull) {
        BigDecimal num = null;
        String input;
        boolean keepGoing = true;
        while (keepGoing){
            try {
                input = this.readString(prompt);
                if (input.trim().equals("") && allowedNull == true) {
                    return null;
                }
                num = new BigDecimal(input);
                if(num.compareTo(BigDecimal.ZERO) < 0) {
                   this.print("This needs to positive.");
                }
                else {
                    keepGoing = false;
                }
            } catch(NumberFormatException e) {
                this.print("This needs to be a valid number.");
            }
        }    
        
        return num;
    }
}
