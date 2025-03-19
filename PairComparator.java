import java.util.Comparator;

/**
 * @author Joshia Nambi
 * A class developed to be able to compare the Pair class
 */
public class PairComparator implements Comparator<Pair> {

    @Override
    public int compare(Pair o1, Pair o2) {
        return o1.compareTo(o2);
    }

    public PairComparator() {
        super();
    }
}