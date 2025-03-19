package lib;

import lib.exceptions.InvalidCharacteristicException;

/**
 * An Animal (Extends LivingBeing)
 * @author Joshia Nambi
 */
public class Animal extends LivingBeing {

    private String species;
    private boolean isPet;
    private final static String[] GENERATED_SPECIES = {"dog", "cat", "ferret", "chicken", "cow", "wolf", "kangaroo",
                                                "emu", "snake", "cockatoo", "koala", "possum", "wallaby"};
    
    final private static int IS_PET = 7;

    /**
     * Constructor to populate it's fields with random variables
     */
    public Animal() {
        super();
        species = GENERATED_SPECIES[(int) (Math.random() * GENERATED_SPECIES.length)];
        if (species.equals("dog") || species.equals("cat") || species.equals("ferret")){
            if (((int) Math.round(Math.random())) == 1) {
                isPet = true;
            } else {
                isPet = false;
            }            
        } else {
            isPet = false;
        }
    }
    /**
     * Construtor to populate fields according to the 
     * @param gender
     * @param age
     * @param bodyType
     * @param species
     * @param isPet
     * @throws InvalidCharacteristicException
     */
    public Animal(String gender, int age, String bodyType, String species, boolean isPet) throws InvalidCharacteristicException {
        super(gender, age, bodyType);
        this.species = species.toLowerCase();

        if (!(species.equals("dog")||species.equals("cat")||species.equals("ferret")) && isPet) {
            throw new InvalidCharacteristicException(IS_PET);
        } else {
            this.isPet = isPet;
        }
    }
    
    @Override
    public String toString() {
        String str = species;
        if (isPet) {
           str += " is pet";
        }
        return str;
    }

    public String getSpecies() {
        return species;
    }

    public String getIsPet() {
        if (isPet) {
            return "pet";
        } else {
            return "non-pet";
        }
    }

    public String[] getArrayRepresentation() {
        String[] arr = {"animal", getSpecies(), getIsPet()};
        return arr;
    }
    @Override
    public String getCSVFormat() {
        return "animal" + "," + getGender() + "," + getAge() + "," + getBodyType() + ",,," + getSpecies() + "," + String.valueOf(isPet);
    }
}
