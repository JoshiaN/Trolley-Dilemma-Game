package lib;

import lib.exceptions.InvalidCharacteristicException;

/**
 * A Human (extends LivingBeing)
 * @author Joshia Nambi
 */
public class Human extends LivingBeing {

    enum Profession {NONE, STUDENT, DOCTOR, PROFESSOR, CRIMINAL, CEO, HOMELESS, LAWYER, ENGINEER, UNEMPLOYED}
    enum AgeCategory {BABY, CHILD, ADULT, SENIOR}

    private Profession profession;
    private boolean pregnant;
    private AgeCategory ageCategory;

    final private static int PROFESSION = 4;
    final private static int PREGNANT = 5;

    /**
     * Constructor used to Randomise Instance variables
     */
    public Human() {
        super();
        //Determine the age category of the person
        if (age > 68) {
            ageCategory = AgeCategory.SENIOR;
        } else if (age >= 17) {
            ageCategory = AgeCategory.ADULT;
        } else if (age >= 5) {
            ageCategory = AgeCategory.CHILD;
        } else {
            ageCategory = AgeCategory.BABY;
        }
        //Pregnant
        if (getGender().equals("female") && ageCategory == AgeCategory.ADULT) {
            if (((int) Math.round(Math.random())) == 1) {
                pregnant = true;
            } else {
                pregnant = false;
            }            
        } else {
            pregnant = false;
        } 
        //Profession
        if (ageCategory != AgeCategory.ADULT) {
            profession = Profession.NONE;
        } else {
            switch ((int) (Math.random() * 9)) {
                case 0:
                    profession = Profession.STUDENT;
                    break;
                case 1:
                    profession = Profession.DOCTOR;
                    break;
                case 2:
                    profession = Profession.PROFESSOR;
                    break;
                case 3:
                    profession = Profession.CRIMINAL;
                    break;
                case 4:
                    profession = Profession.CEO;
                    break;
                case 5:
                    profession = Profession.HOMELESS;
                    break;
                case 6:
                    profession = Profession.LAWYER;
                    break;
                case 7:
                    profession = Profession.ENGINEER;
                    break;            
                default:
                    profession = Profession.UNEMPLOYED;
                    break;
            }
        }
    }

    /**
     * Constructs human based upon parameters
     * @param gender
     * @param age
     * @param bodyType
     * @param profession
     * @param pregnant
     * @throws InvalidCharacteristicException
     */
    public Human(String gender, int age, String bodyType, String profession, boolean pregnant) throws InvalidCharacteristicException {
        super(gender, age, bodyType);
        if (age > 68) {
            ageCategory = AgeCategory.SENIOR;
        } else if (age >= 17) {
            ageCategory = AgeCategory.ADULT;
        } else if (age >= 5) {
            ageCategory = AgeCategory.CHILD;
        } else {
            ageCategory = AgeCategory.BABY;
        }

        try {
            //Only adults can have a profession
            if (ageCategory != AgeCategory.ADULT && (!profession.toLowerCase().equals("none"))) {
                throw new InvalidCharacteristicException(PROFESSION);
            } else {
                this.profession = Profession.valueOf(profession.toUpperCase());
            }            
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidCharacteristicException(PROFESSION);
        }

        //Only ADULT females can be pregnant
        if (!gender.toLowerCase().equals("female") && pregnant) {
            throw new InvalidCharacteristicException(PREGNANT);
        } else if (ageCategory != AgeCategory.ADULT && pregnant && gender.toLowerCase().equals("female")) {
            throw new InvalidCharacteristicException(PREGNANT);
        } else {
            this.pregnant = pregnant;
        }                 
    }

    /**
     * Retursn a string representation of the Human
     */
    @Override
    public String toString() {
        String str = bodyType.name().toLowerCase() + " " + ageCategory.name().toLowerCase() + " ";
        if (profession != Profession.NONE) {
            str += profession.name().toLowerCase() + " ";
        }
        str += gender.name().toLowerCase();
        if (pregnant) {
            str += " pregnant";
        }
        return str;
    }

    public String getProfession() {
        return profession.name().toLowerCase();
    }

    private String getPregnant() {
        if (pregnant) {
            return "pregnant";
        } else {
            return "non-pregnant";
        }
    }

    public String getAgeCategory(){
        return ageCategory.name().toLowerCase();
    }

    public String[] getArrayRepresentation() {
        String[] arr = {"human", getGender(), getAgeToString(), getAgeCategory(), getBodyType(), getProfession(), getPregnant()};
        return arr;
    }

    @Override
    public String getCSVFormat() {
        return "human," + getGender() + "," + getAge() + "," + getBodyType() + "," + getProfession() + "," + String.valueOf(pregnant) + ",,";
    }

}
