import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author: Joshia Nambi
 * student id: 1448501 
 * student email: joshia.nambi@student.unimelb.edu.au
 */
public class RescueBot {

    /**
     * Given a scenario, chooses to save the characters at a particular location using a RescueBot based on some character parameters
     * @param Scenario scenario: the ethical dilemma
     * @return Decision: which location to send RescueBot to
     */
    public static Location decide(Scenario scenario) {
        //Reduce the likliness to save the people in the scenario if they have tresspassed
        final double TRESSPASS_PENALITY = 0.5;    //1
        final HashMap<String,Double> CHARACTERISTIC_VALUES = new HashMap<>();
        //Pregnant Status
        CHARACTERISTIC_VALUES.put("pregnant", 1.0);
        //If the animal is a pet
        CHARACTERISTIC_VALUES.put("pet", 0.75);
        //The age category of the human
        CHARACTERISTIC_VALUES.put("baby", 1.0);
        CHARACTERISTIC_VALUES.put("child", 0.75);
        CHARACTERISTIC_VALUES.put("adult", 0.5);
        CHARACTERISTIC_VALUES.put("senior", 0.25);
        //The body type
        CHARACTERISTIC_VALUES.put("athletic", 1.0);
        CHARACTERISTIC_VALUES.put("average", 0.5);
        CHARACTERISTIC_VALUES.put("overweight", 0.25);
        //Select professions should get priority
        CHARACTERISTIC_VALUES.put("doctor", 1.0);
        CHARACTERISTIC_VALUES.put("professor", 1.0);
        CHARACTERISTIC_VALUES.put("engineer", 1.0);
        CHARACTERISTIC_VALUES.put("lawyer", 1.0);
        CHARACTERISTIC_VALUES.put("student", 0.8);
        CHARACTERISTIC_VALUES.put("criminal", -1.0);
        CHARACTERISTIC_VALUES.put("ceo", 0.7);
        CHARACTERISTIC_VALUES.put("unemployed", -0.5);

        int locationCount = scenario.getNumberOfLocations();
        double[] likelinessToSave = new double[locationCount];
        double maxLikelinessToSave = 0;
        int indexOfMaxLikelinessToSave = 0;
        
        //For each location
        for (int i = 0; i < locationCount; i++) {
            Location currentLocation = scenario.getLocation(i);
            //For each character
            for (int j = 0; j < currentLocation.getNumberOfCharacters(); j++) {
                String[] attributes = currentLocation.getCharacter(j).toString().split(" ");
                for (String characteristic : attributes) {
                    if (CHARACTERISTIC_VALUES.containsKey(characteristic)) {
                        likelinessToSave[i] += CHARACTERISTIC_VALUES.get(characteristic);
                    }
                }
            }
            if (currentLocation.getIllegalEntry()) {
                likelinessToSave[i] = likelinessToSave[i] * TRESSPASS_PENALITY;
            }
            if (likelinessToSave[i] > maxLikelinessToSave) {
                maxLikelinessToSave = likelinessToSave[i];
                indexOfMaxLikelinessToSave = i;
            }
        }
        return scenario.getLocation(indexOfMaxLikelinessToSave);
    }

    /**
     * This functional is called when the program is started. 
     * Checks the arguments provided, via command-line, and then processes commands and checks for invalid commands
     * @param args parameters passed in via command-line
     */
    public static void main(String[] args) {
        File scenariosFile = null;
        String logPath = null;

        for(int i = 0; i < args.length; i++) {
            switch (args[i]) {
                //Shows the help menu
                case "--help": 
                case "-h":
                    printHelp();
                    break;
                //Checks arguments for scenarios
                case "-s":
                case "--scenarios":
                    if(i+1 < args.length) {
                        try {
                            scenariosFile = new File(args[++i]);
                            if(!scenariosFile.exists()) {
                                throw new FileNotFoundException("could not find scenarios file.");
                            }

                        } catch (FileNotFoundException e) {
                            System.out.println("java.io.FileNotFoundException: " + e.getMessage());
                            printHelp();
                        }
                    } else {
                        printHelp();
                    }
                    break;
                //Checks arguments for logging data
                case "-l":
                case "--log":
                    if (i+1 < args.length) {
                        logPath = args[++i];
                    } else {
                        printHelp();
                    }
                    break;
                //A wrong argument is provded
                default:
                    printHelp();
                    break;
            }
        }

        //At this point, it can be assumed that all the arguments have be processed and are valid according to specifications

        if (scenariosFile != null && logPath == null) {
            new MainMenu(scenariosFile);
        } else if (scenariosFile != null && logPath != null) {
            new MainMenu(scenariosFile, logPath);
        } else if (scenariosFile == null && logPath != null) {
            new MainMenu(logPath);
        } else {
            new MainMenu();
        }
    }

    /**
     * A helper function that prints out the help menu and then exits the program with an exit status of 1 (to indicate failure)
     */
    private static void printHelp() {
        System.out.println("RescueBot - COMP90041 - Final Project\n");
        System.out.println("Usage: java RescueBot [arguments]\n");
        System.out.println("Arguments:");
        System.out.println("-s or --scenarios\tOptional: path to scenario file");
        System.out.println("-h or --help\t\tOptional: Print Help (this message) and exit");
        System.out.println("-l or --log\t\tOptional: path to data log file");
        System.exit(1);
    }
}