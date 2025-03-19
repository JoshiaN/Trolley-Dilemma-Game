package lib.exceptions;

/**
 * A custom defined exception for the RescueBot program
 * Thrown when a file being accessed is empty
 * @author Joshia Nambi
 */
public class EmptyFileException extends Exception {
    
    public EmptyFileException() {
        super("EmptyFileException: The file being accessed is Empty.");
    }
}
