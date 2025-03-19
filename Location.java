import java.util.ArrayList;

import lib.*;
import lib.exceptions.InvalidCharacteristicException;

/**
 * A location where people need rescuing using the RescueBot
 * @author Joshia Nambi
 */
public class Location {
    
    private String longitude;
    private String latitude;
    private ArrayList<LivingBeing> characters; 
    private boolean illegalEntry;
    private final static int MAX_CHARACTERS_TO_GENERATE = 6;
    private final static int MIN_CHARACTERS_TO_GENERATE = 1;

    /**
     * Default constructor to generate a random location with random characters
     */
    public Location() {
        characters = new ArrayList<>();
        //GENERATE LONGITUDE
        double longValue =  Math.round((Math.random() * 360) * 100) /100 - 180;
        if (((int) Math.round(Math.random())) == 1) {
            longitude = longValue + " W";
        } else {
            longitude = longValue + " E";
        }
        //GENERATE LATITUDE
        double latValue = Math.round((Math.random() * 180) * 100) /100 - 90;
        if (((int) Math.round(Math.random())) == 1) {
            latitude = latValue + " N";
        } else {
            latitude = latValue + " S";
        }
        //GENERATE TRESPASSING
        if (((int) Math.round(Math.random())) == 1) {
            illegalEntry = true;
        } else {
            illegalEntry = false;
        }
        //GENERATE CHARACTERS
        int range = MAX_CHARACTERS_TO_GENERATE - MIN_CHARACTERS_TO_GENERATE;
        int noOfCharactersToGenerate = (int)(Math.random() * range + MIN_CHARACTERS_TO_GENERATE);
        for (int i = 0; i < noOfCharactersToGenerate; i++) {
            if (((int) Math.round(Math.random())) == 1) {
                characters.add(new Human());
            } else {
                characters.add(new Animal());
            }            
        }
    }

    /**
     * Constructor to initialise a location with the associated details
     * @param longitude longitude of location
     * @param latitude  latitude of location
     * @param entryType whether the characters were tresspassing or legally accessing the location
     */
    public Location(String latitude, String longitude,String entryType) {
        this.longitude = longitude;
        this.latitude = latitude;
        characters = new ArrayList<>();
        if(entryType.equals("trespassing")) {
            illegalEntry = true;
        } else if (entryType.equals("legal")) {
            illegalEntry = false;
        }
    }

    /**
     * Adds a character to this location
     * @param rawCharacterString   The raw string from the scenarios.csv file to initialise a character
     * @throws NumberFormatException    thrown when a string cannot be parsed to an integer (namely the age of the character)
     * @throws InvalidCharacteristicException   thrown when atleast one of the characteristics of the character is not suitable
     */
    public void addCharacterToLocation(String[] rawCharacterString) throws NumberFormatException, InvalidCharacteristicException {
        String gender = rawCharacterString[1];
        int age = Integer.parseInt(rawCharacterString[2]);
        String bodyType = rawCharacterString[3];
        if (rawCharacterString[0].equals("human")) {
            //Create a human character
            String profession = rawCharacterString[4];
            boolean pregnant;
            if (rawCharacterString[5].toLowerCase().equals("true") || rawCharacterString[5].toLowerCase().equals("false")) {
                pregnant = Boolean.parseBoolean(rawCharacterString[5]);
            } else {
                throw new InvalidCharacteristicException(5);
            }
            characters.add(new Human(gender, age, bodyType, profession, pregnant));
        } else if (rawCharacterString[0].equals("animal")) {
            //Create an animal character
            String species = rawCharacterString[6];
            boolean isPet;
            if (rawCharacterString[7].toLowerCase().equals("true") || rawCharacterString[7].toLowerCase().equals("false")) {
                isPet = Boolean.parseBoolean(rawCharacterString[7]);
            } else {
                throw new InvalidCharacteristicException(7);
            }
            characters.add(new Animal(gender, age, bodyType, species, isPet));
        } else {
            //Do Nothing
            //There is an error as neither human nor animal
            //Skips the line provided
        }
    }

    /**
     * @return the coordinates of this location as a string
     */
    public String getCoordinatesToString() {
        return latitude + ", " + longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    /**
     * @return the entry status of the characters as a string
     */
    public String getEntryStatusToString() {
        if (illegalEntry) {
            return "Trespassing: yes";
        } else {
            return "Trespassing: no";
        }
    }

    /**
     * @return the entry status of the characters as a boolean
     */
    public boolean getIllegalEntry() {
        return illegalEntry;
    }

    /**
     * @param i index of character to be returned
     * @return  character to be returned
     */
    public LivingBeing getCharacter(int i) {
        return characters.get(i);
    }

    /**
     * @return number of characters at this location
     */
    public int getNumberOfCharacters() {
        return characters.size();
    }

    /**
     * @return the string representation of all the characters
     */
    public String getCharactersToString() {
        String msg = characters.size() + " Characters: \n";
        for (LivingBeing character : characters) {
            msg += "- " +  character.toString() + "\n";
        }
        return msg;
    }
}