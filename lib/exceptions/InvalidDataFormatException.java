package lib.exceptions;

import java.util.zip.DataFormatException;

/**
 * A custom defined exception for the RescueBot program
 * This Exception is thrown when the data format read in does not match the specifications
 * @author Joshia Nambi
 */
public class InvalidDataFormatException extends DataFormatException {
    
    public InvalidDataFormatException() {
        super("WARNING: invalid data format in scenarios file in line ");
    }
}
