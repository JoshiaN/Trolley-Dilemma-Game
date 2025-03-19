package lib.exceptions;

/**
 * A custom defined exception for the RescueBot program
 * A character in the RescueBot program throws this exception when an invalid attribute value is provided
 * @author Joshia Nambi
 */
public class InvalidCharacteristicException extends Exception {

    private int errorColumn = -1;

    public InvalidCharacteristicException(int errorColumn) {
        super("WARNING: invalid characteristic in scenarios file");
        this.errorColumn = errorColumn;
    }

    /**
     * @return the index of the column in which an invalid attribute was detected
     */
    public int getIndexOfColumnWithError() {
        return errorColumn;
    }
}
