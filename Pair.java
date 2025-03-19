/**
 * A class developed to store the characteristics of the characters and then calculate the survival ratio
 * @author Joshia Nambi
 */

public class Pair implements Comparable<Pair> {
    
    private String attribute;
    private int saved = 0;
    private int total = 0;

    public Pair(String attribute) {
        this.attribute = attribute;
    }

    /**
     * Compares this pair to another pair
     * Use to develop the comparator class
     */
    public int compareTo(Pair otherPair) {
        if (this.getSurvivalRatio() > otherPair.getSurvivalRatio()) {
            return -1;
        } else if (this.getSurvivalRatio() < otherPair.getSurvivalRatio()) {
            return 1;
        } else {
            //Numbers are equal
            return this.getAttribute().compareTo(otherPair.getAttribute());
        }
    }  

    /**
     * Called to indicate that a character with an attribute was saved
     */
    public void addSavedAttribute() {
        saved++;
        total++;
    }

    /**
     * Invoked to indicate that a character with an attribute was not saved
     */
    public void addPerishedAttribute() {
        total++;
    }

    /**
     * Adds a sum to the saved variables
     * @param i the number to add
     */
    public void sumSaved(int i) {
        saved += i;
    }

    /**
     * Converts object to string
     */
    @Override
    public String toString() {
        return attribute + ": " + getSurvivalRatio();
    }


    public String getAttribute() {
        return attribute;
    }

    /**
     * Calculate the survial ratio of the attribute
     * @return the survival ratio of the attribute
     */
    public double getSurvivalRatio() {
        double ratio = (double) saved / total;
        return (double) Math.ceil(ratio * 100) / 100;
    } 
}
