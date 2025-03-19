package lib;

import lib.exceptions.InvalidCharacteristicException;
import java.lang.Math;

/**
 * A class the represents an entity that lives
 * @author Joshia Nambi
 */
public abstract class LivingBeing {
    
    enum Gender {MALE, FEMALE, UNKNOWN};
    enum BodyType {OVERWEIGHT, ATHLETIC, AVERAGE, UNSPECIFIED};

    protected Gender gender;
    protected int age;
    protected BodyType bodyType;

    private final static int MAX_GENERATED_AGE = 80;

    //Constants for Column Numbers
    final private static int GENDER = 1;
    final private static int AGE = 2;  
    final private static int BODY_TYPE = 3;    

    /**
     * Constructor used to randomise the instance variables
     */
    public LivingBeing() {
        //Randomise the Gender of the LivingBeing
        if (((int) Math.round(Math.random())) == 1) {
            gender = Gender.FEMALE;
        } else {
            gender = Gender.MALE;
        }
        //Randomise the age of the LivingBeing (0 to 80)
        age = (int) (Math.random() * MAX_GENERATED_AGE);
        //Randomise the BodyType
        switch ((int) (Math.random() * 3)) {
            case 0:
                bodyType = BodyType.OVERWEIGHT;
                break;
            case 1:
                bodyType = BodyType.ATHLETIC;
                break;
            default:
                bodyType = BodyType.AVERAGE;
                break;
        }
    }
    
    /**
     * Constructor that creates a LivingBeing according to parameters
     * @param gender gender of the livingbeing
     * @param age age of the living being
     * @param bodyType bodytype of the living being
     * @throws InvalidCharacteristicException
     */
    public LivingBeing(String gender, int age, String bodyType) throws InvalidCharacteristicException {
        try {
            this.gender = Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidCharacteristicException(GENDER);
        }
        try {
            if (age >= 0) {
                this.age = age;
            } else {
                throw new InvalidCharacteristicException(AGE);
            }
            this.bodyType = BodyType.valueOf(bodyType.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidCharacteristicException(BODY_TYPE);
        }
    }

    public String getGender() {
        return gender.name().toLowerCase();
    }

    public int getAge() {
        return age;
    }

    protected String getAgeToString() {
        return Integer.toString(age);
    }

    public String getBodyType() {
        return bodyType.name().toLowerCase();
    }

    /**
     * Method to represent this class as an array
     * @return
     */
    public abstract String[] getArrayRepresentation();

    public abstract String getCSVFormat();
}
