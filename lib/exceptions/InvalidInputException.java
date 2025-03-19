package lib.exceptions;

/**
 * A custom defined exception for the RescueBot program
 * Thrown when a user provides an invalid input to the RescueBot program
 * @author Joshia Nambi
 */
public class InvalidInputException extends Exception {

    public InvalidInputException() {
        super("Invalid Response! ");
    }
}
