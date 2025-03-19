import java.util.ArrayList;

import lib.exceptions.InvalidCharacteristicException;


/**
 * A class that represents a particular scenario
 * @author Joshia Nambi
 */
public class Scenario {

    private String naturalDisaster;
    private ArrayList<Location> disasterLocations;
    private final static String[] GENERATED_DISASTER_NAMES = {"flood", "cyclone", "earthquake", "bushfire", "tsunami", "tornado"}; 
    private final static int MIN_LOCATIONS_GENERATED = 2;
    private final static int MAX_LOCATIONS_GENERATED = 4;   

    /**
     * Contructor to randomly generate a scenario with a random number of locations
     */
    public Scenario() {
        //GENERATE NATURAL DISASTER NAME
        naturalDisaster = GENERATED_DISASTER_NAMES[(int) (Math.random() *GENERATED_DISASTER_NAMES.length)];
        //GENERATE DISASTER LOCATIONS
        disasterLocations = new ArrayList<Location>();
        int range = MAX_LOCATIONS_GENERATED-MIN_LOCATIONS_GENERATED;
        int numberOfLocationsGenerated = (int) (Math.random() * range + MIN_LOCATIONS_GENERATED);
        for (int i = 0; i < numberOfLocationsGenerated; i++) {
            disasterLocations.add(new Location());
        }
    }

    /**
     * Generates a pre-defined scenario
     * @param naturalDisaster the natural disaster occuring
     */
    public Scenario(String naturalDisaster) {
        this.naturalDisaster = naturalDisaster;
        disasterLocations = new ArrayList<Location>();
    }

    /**
     * Add location to this scenario
     * @param rawLocationData Data from scenarios file indicating the longitude, latitude and tresspassing status
     */
    public void addLocation(String rawLocationData) {
        String[] locationData = rawLocationData.split(";");
        disasterLocations.add(new Location(locationData[0], locationData[1], locationData[2]));
    }

    /**
     * Add a character to the previously added Location
     * @param rawCharacterData data from the scenarios file
     * @throws NumberFormatException
     * @throws InvalidCharacteristicException
     */
    public void addCharacter(String[] rawCharacterData) throws NumberFormatException, InvalidCharacteristicException {
        disasterLocations.get(disasterLocations.size() - 1).addCharacterToLocation(rawCharacterData);
    } 
    
    public Location getLocation(int i) {
        return disasterLocations.get(i);
    }

    public int getIndexOfLocation(Location loc) {
        return disasterLocations.indexOf(loc);
    }

    /**
     * Prints the current scenario to the console
     */
    public void printScenario() {
        System.out.println("======================================");
        System.out.println("# Scenario: " + naturalDisaster);
        System.out.println("======================================");

        for (int i = 0; i < disasterLocations.size(); i++) {
            System.out.printf("[%d] Location: ", i+1);
            System.out.println(disasterLocations.get(i).getCoordinatesToString());
            System.out.println(disasterLocations.get(i).getEntryStatusToString());
            System.out.print(disasterLocations.get(i).getCharactersToString());
        }
    }

    public int getNumberOfLocations() {
        return disasterLocations.size();
    }

    public String getDisaster() {
        return naturalDisaster;
    }
}
