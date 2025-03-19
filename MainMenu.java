import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import lib.exceptions.*;

/**
 * This class represents the main-menu of the RescueBot program and is responsible for control flow of the menu system
 * @author: Joshia Nambi
 * student id: 1448501
 */
public class MainMenu {

    final private static String WELCOME_FILE = "welcome.ascii"; 
    private ArrayList<Scenario> scenarios;          //Stores all the scenarios
    private Scanner inputScanner;                   //Scanner to capture data from the console, as the user interacts with the program
    private String logFilePath = "rescuebot.log";   //The path to the logfile, the default is already provided and is overwritten when another is provided
    private boolean importedScenarios = false;      //Tracks whether scenarios were provided to the program

    /**
     * Contructor when no arguments are provided to the RescueBot program
     */
    public MainMenu() {
        showWelcomeMessage();
        inputScanner = new Scanner(System.in);
        showMenuOptions();
    }

    /**
     * Contructor when only the scenario file is provided to the RescueBot program
     * @param scenariosFile the File to import scenarios, locations and characters from 
     */
    public MainMenu(File scenariosFile) {
        showWelcomeMessage();
        scenarios = new ArrayList<Scenario>();
        importScenarios(scenariosFile);
        inputScanner = new Scanner(System.in);
        showMenuOptions();
    }

    /**
     * Constructor when scenarios file and log file is provided to the RescueBot program
     * @param scenariosFile the File to import scenarios, locations and characters from 
     * @param logPath the path of the logfile
     */
    public MainMenu(File scenariosFile, String logPath) {
        showWelcomeMessage();
        scenarios = new ArrayList<Scenario>();
        importScenarios(scenariosFile);
        logFilePath = logPath;
        inputScanner = new Scanner(System.in);
        showMenuOptions();
    }

    /**
     * Constructor when only the log file is provided to the RescueBot program
     * @param logPath the path of the logfile
     */
    public MainMenu(String logPath) {
        showWelcomeMessage();
        inputScanner = new Scanner(System.in);
        logFilePath = logPath;
        showMenuOptions();
    }

    /**
     * Shows the welcome message of the RescueBot program, by reading in and printing the contents of the welcome.ascii file
     */
    private void showWelcomeMessage() {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(WELCOME_FILE));
            String line = null;
            while ((line = fileReader.readLine()) != null) {
                System.out.println(line);
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("IO Exception: Could not find the " + WELCOME_FILE + " file");
            System.exit(1);
        }
    }

    /**
     * Reads in the scenario file that was passed to the RescueBot program and adds the predefined scenarios, locations and characters
     * Recovers from basic anamalies in the data such as:
     *  > Incorrect number of columns: skips and prints error warning
     *  > Incorrect attributes of chracters: sets the corresponding attribute to a default value and prints error warning
     * Finally prints the number of scenarions that have been imported
     * @param scenariosFile the File to import scenarios, locations and characters from 
     */
    private void importScenarios(File scenariosFile) {
        // The default values for character attributes
        final String DFLT_GENDER = "unknown";
        final String DFLT_AGE = "0";
        final String DFLT_BODYTYPE = "unspecified";
        final String DFLT_PROFESSION = "none";
        final String DFLT_PREGNANT = "false";
        final String DFLT_ISPET = "false";

        Scanner scenarioReader = null;
        try {
            scenarioReader = new Scanner(scenariosFile);
        } catch (FileNotFoundException e) {
            //Already handled in RescueBot.java, so unlikely there will be an error
            System.out.println("java.io.FileNotFoundException: could not find scenarios file.");
            System.exit(1);
        }

        //Constats for Column Numbers
        final int GENDER = 1;
        final int AGE = 2;  //This might need to be implemented????
        final int BODY_TYPE = 3;
        final int PROFESSION = 4;
        final int PREGNANT = 5;
        final int IS_PET = 7;

        int lineNumber = 0;
        while (scenarioReader.hasNextLine()) {
            lineNumber++;
            String lineString = scenarioReader.nextLine();
            int numberOfColumns = commaCounter(lineString);

            boolean hasLineParsedSuccessfully = false;
            // Converts the line to an array
            String[] lineArray = lineString.split(",", 8);

            while (!hasLineParsedSuccessfully) {
                try {
                    // Make sure there are 8 Columns in the row
                    // There are 8 columns when the number of commas is equal to 7
                    if (numberOfColumns != 7) {
                        throw new InvalidDataFormatException();
                    }
                    // Skips the header line
                    else if (lineArray[0].length() == 0) {
                        hasLineParsedSuccessfully = true;
                    }
                    // Makes a new scenario object and adds it to the scenarios arraylist
                    else if (lineArray[0].startsWith("scenario:")) {
                        String disaster = lineArray[0].substring(9);
                        scenarios.add(new Scenario(disaster));
                        hasLineParsedSuccessfully = true;
                    }
                    // Adds a location to the previously added scenario
                    else if (lineArray[0].startsWith("location:")) {
                        String locData = lineArray[0].substring(9);
                        scenarios.get(scenarios.size() - 1).addLocation(locData);
                        hasLineParsedSuccessfully = true;
                    }
                    // Once all other checks are done, the characters are added to the previously added scenario (and in turn the previously added location)
                    else {
                        scenarios.get(scenarios.size() - 1).addCharacter(lineArray);
                        hasLineParsedSuccessfully = true;                        
                    }

                } catch (InvalidDataFormatException e) {
                    System.out.println(e.getMessage() + lineNumber);
                    // Displays warning and then skips line
                    hasLineParsedSuccessfully = true;
                } catch (NumberFormatException e) {
                    System.out.println("WARNING: invalid number format in scenarios file in line " + lineNumber);
                    lineArray = elementReplace(lineArray, AGE, DFLT_AGE);
                } catch (InvalidCharacteristicException e) {
                    System.out.println(e.getMessage() + " in line " + lineNumber);

                    switch (e.getIndexOfColumnWithError()) {
                        case GENDER:
                            lineArray = elementReplace(lineArray, GENDER, DFLT_GENDER);
                            break;
                        case AGE:
                            lineArray = elementReplace(lineArray, AGE, DFLT_AGE);
                            break;
                        case BODY_TYPE:
                            lineArray = elementReplace(lineArray, BODY_TYPE, DFLT_BODYTYPE);
                            break;
                        case PROFESSION:
                            lineArray = elementReplace(lineArray, PROFESSION, DFLT_PROFESSION);
                            break;
                        case PREGNANT:
                            lineArray = elementReplace(lineArray, PREGNANT, DFLT_PREGNANT);
                            break;
                        case IS_PET:
                            lineArray = elementReplace(lineArray, IS_PET, DFLT_ISPET);
                            break;                 
                        default:
                            break;
                    }
                }
            }
        }
        System.out.println(scenarios.size() + " scenarios imported.");
        scenarioReader.close();
        importedScenarios = true;
    }

    /**
     * Replaces the element at index in array arr with String replacement
     * @param arr array to have element replaced
     * @param index index of element to be replaced
     * @param replacement replacment element
     * @return the original array with the element replaced
     */
    private String[] elementReplace(String[] arr, int index, String replacement) {
        ArrayList<String> arrLst = new ArrayList<String>(Arrays.asList(arr));
        arrLst.set(index, replacement);
        String[] arr1 = new String[8];
        for (int i = 0; i < arr.length; i++) {
            arr1[i] = arrLst.get(i);
        }
        return arr1;
    }

    /**
     * Counts the number of commas (,) in an expression
     * @param expression the string where the commas should be counted
     * @return number of commas counted
     */
    private int commaCounter(String expression) {
        int count = 0;
        char comma = ',';
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == comma) {
                count++;
            }
        }
        return count;
    }

    /**
     * Shows the menu options of the program to the user.
     * Uses user input to navigate the program
     */
    private void showMenuOptions() {
        // Check implementation of the inputScanner
        while (true) {
            System.out.println("Please enter one of the following commands to continue:");
            System.out.println("- judge scenarios: [judge] or [j]");
            System.out.println("- run simulations with the in-built decision algorithm: [run] or [r]");
            System.out.println("- show audit from history: [audit] or [a]");
            System.out.println("- quit the program: [quit] or [q]");
            System.out.print("> ");
            String menuChoice = inputScanner.next();
            switch (menuChoice) {
                case "judge":
                case "j":
                    judgeScenarios();
                    break;
                case "run":
                case "r":
                    runSimulation();
                    break;
                case "audit":
                case "a":
                    conductAudit();
                    break;
                case "quit":
                case "q":
                    System.exit(0);
                    break;
                default: // WRONG OPTION
                    System.out.print("Invalid command! ");
                    break;
            }
        }
    }

    /**
     * generates random scenarios according to the number required
     * @param numOfScenariosToGenerate the number of scenarios to be generated
     */
    private void generateScenarios(int numOfScenariosToGenerate) {
        scenarios = new ArrayList<Scenario>();
        for (int i = 0; i < numOfScenariosToGenerate; i++) {
            scenarios.add(new Scenario());
        }
    }

    /**
     * Conducts the audit on the user and algorithm judgement choices
     */
    private void conductAudit() {
        try {
            Auditor conductAudit = new Auditor();
            conductAudit.readLogFile(logFilePath);;
            conductAudit.printAudit();           
            System.out.print("That's all. ");
        } catch (FileNotFoundException | EmptyFileException e) {
            System.out.println("No history found. ");
        }
        //End
        System.out.println("Press Enter to return to main menu.");
        System.out.print("> ");
        inputScanner.nextLine();
        inputScanner.nextLine();
    }

    /**
     * Asks user for consent 
     * If scenarios are not loaded in, generates scenarios
     * prints the scenario to the screen and asks user to judge when location to save
     * Saves the scenario to the log file
     * Asks user if they want to continue judging or to stop judging
     */
    private void judgeScenarios() {
        //Ask the user for consent to store their data
        boolean consentToStoreData = false;
        boolean consentAnswered = false;
        while (!consentAnswered) {
            System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
            System.out.print("> ");
            String consentInput = inputScanner.next();
            try {
                switch (consentInput) {
                    case "yes":
                        consentAnswered = true;
                        consentToStoreData = true;
                        break;
                    case "no":
                        consentAnswered = true;
                        consentToStoreData = false;
                        break;
                    default:
                        throw new InvalidInputException();
                }
            } catch (InvalidInputException e) {
                System.out.print(e.getMessage());
            }
        }
        //Present the scenarios to the user to be judged
        int currentScenarioIndex = 0;
        StatisticsGenerator statGenerator = new StatisticsGenerator();
        boolean continueJudging = true;
        while (continueJudging) {
            //Check if scenarios have been imported, if they have do not generate new scenarios however if not then generate 3
            if (!importedScenarios) {
                generateScenarios(3);
                currentScenarioIndex = 0;
            }
            ArrayList<Scenario> scenariosToBeLogged = new ArrayList<>();
            ArrayList<Integer> savedLocations = new ArrayList<>();
            //Loop until all scenarios have been processed or three scenarios have been processed (whichever comes first)
            for (int i = 0; i < 3 && currentScenarioIndex < scenarios.size(); i++, currentScenarioIndex++) {
                // Print the current scenario to the console
                scenarios.get(i).printScenario();
                // Ask user where to deploy the RescueBot and check their input
                boolean validDecision = false;
                while (!validDecision) {
                    try {
                        System.out.println("To which location should RescueBot be deployed?");
                        System.out.print("> ");
                        int input = Integer.parseInt(inputScanner.next());
                        if (input > 0 && input <= scenarios.get(i).getNumberOfLocations()) {
                            // Saves the characters at this location
                            validDecision = true;
                            // PASS OBJECT TO STATISTICS CLASS
                            statGenerator.parseScenarioForStatistics(scenarios.get(i), input-1);
                            //Add data to be logged
                            scenariosToBeLogged.add(scenarios.get(i));
                            savedLocations.add(input-1);
                        } else {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid Response! ");
                    }
                }
            }
            statGenerator.printStatistics("Statistic");
            //Terminate if all scenarios have been presented to the User

            //SAVE JUDGED STATISTICS
            if(consentToStoreData) {
                //Write the scenarios to the logfile
                statGenerator.saveStatisticsToLogFile(logFilePath, scenariosToBeLogged, savedLocations, "user");
            }

            boolean moreScenariosToJudge = currentScenarioIndex < scenarios.size()-1;
            if (moreScenariosToJudge || !importedScenarios) {
                boolean validUserResponse = false;
                while (!validUserResponse) {
                    System.out.println("Would you like to continue? (yes/no)");
                    System.out.print("> ");
                    String presentMoreScenarios = inputScanner.next();
                    switch (presentMoreScenarios) {
                        case "yes":
                            continueJudging = true;
                            validUserResponse = true;
                            break;
                        case "no":
                            continueJudging = false;
                            validUserResponse = true;
                            break;
                        default:
                            System.out.print("Invalid response! ");
                            break;
                    }
                }                
            }
            if (!continueJudging || (!moreScenariosToJudge && importedScenarios)) {
                System.out.println("That's all. Press Enter to return to main menu.");
                System.out.print("> ");
                inputScanner.nextLine();
                inputScanner.nextLine();
                continueJudging = false;
            }
        }
    }

    /**
     * Runs a simulation on imported scenarios (or if not imported generates a number of scenarios after asking the user)
     * Prints the results to the screen and then saves to the logfile
     */
    private void runSimulation() {
        StatisticsGenerator statGenerator = new StatisticsGenerator();
        if (!importedScenarios) {
            boolean validNumberOfScenarios = false;
            while (!validNumberOfScenarios) {
                System.out.println("How many scenarios should be run?");
                System.out.print("> ");
                try {
                    int input = Integer.parseInt(inputScanner.next());
                    if (input >= 2) {
                        validNumberOfScenarios = true;
                        generateScenarios(input);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Invalid Input! ");
                }
            }            
        }
        ArrayList<Integer> savedLocations = new ArrayList<>();

        for (int i = 0; i < scenarios.size(); i++) {
            Scenario currentScenario = scenarios.get(i);
            Location savedLocation = RescueBot.decide(currentScenario);
            int savedLocationIndex = currentScenario.getIndexOfLocation(savedLocation);
            savedLocations.add(savedLocationIndex);
            statGenerator.parseScenarioForStatistics(currentScenario, savedLocationIndex);
        }
        //Generate the statistics
        statGenerator.printStatistics("Statistic");
        //Write to LogFile
        statGenerator.saveStatisticsToLogFile(logFilePath, scenarios, savedLocations, "algorithm");
        //End
        System.out.println("That's all. Press Enter to return to main menu.");
        System.out.print("> ");
        inputScanner.nextLine();
        inputScanner.nextLine();
    }
}
