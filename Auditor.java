import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import lib.exceptions.EmptyFileException;
import lib.exceptions.InvalidCharacteristicException;

/**
 * A Class that generates the audit of the RescueBot program from it's log file
 * @author Joshia Nambi
 */
public class Auditor {
    
    private ArrayList<Scenario> userDecidedScenarios;
    private ArrayList<Integer> userSavedLocations;
    private ArrayList<Scenario> algorithmDecidedScenarios;
    private ArrayList<Integer> algorithmSavedLocations;

    public Auditor() {
        userDecidedScenarios = new ArrayList<>();
        algorithmDecidedScenarios = new ArrayList<>();
        userSavedLocations = new ArrayList<>();
        algorithmSavedLocations = new ArrayList<>();
    }

    /**
     * Print the Audit of the Algorithm and the User to the Console
     */
    public void printAudit() {
        if (algorithmDecidedScenarios.size() > 0) {
            printStatisticsForAlgorithm();
        }

        if (userDecidedScenarios.size() > 0) {
            System.out.println();
            printStatisticsForUser();
        }
    }
    /**
     * Print the statistics for the user to the Console
     */
    private void printStatisticsForUser() {
        StatisticsGenerator userStatistics = new StatisticsGenerator();
        for (int i = 0; i < userDecidedScenarios.size(); i++) {
            userStatistics.parseScenarioForStatistics(userDecidedScenarios.get(i), userSavedLocations.get(i));
        }
        userStatistics.printStatistics("User Audit");
    }

    /**
     * Print the statistics for the algorithm to the Console
     */
    private void printStatisticsForAlgorithm() {
        StatisticsGenerator algorithmStatistics = new StatisticsGenerator();
        for (int i = 0; i < algorithmDecidedScenarios.size(); i++) {
            algorithmStatistics.parseScenarioForStatistics(algorithmDecidedScenarios.get(i), algorithmSavedLocations.get(i));
        }
        algorithmStatistics.printStatistics("Algorithm Audit");
    }

    /**
     * Reads the specified logfile at the path provided and recreates the scenarios that were decided to be able to build a final statistic
     * @param logFilePath the path of the log file
     * @throws FileNotFoundException thrown when file cannot be found at path
     * @throws EmptyFileException when file does not exist at path
     */
    public void readLogFile(String logFilePath) throws FileNotFoundException, EmptyFileException {

        File logFile = new File(logFilePath);
        if (!(logFile.length() > 0)) {
            throw new EmptyFileException();
        }

        Scanner logReader = new Scanner(new File(logFilePath));

        while (logReader.hasNextLine()) {
            String[] lineArray = logReader.nextLine().split(",", 8);
            //Skips the header line and splitting line
            if (lineArray[0].equals(">>RescueBot Log File<<") || lineArray[0].equals("-----")) {
                continue;
            } 
            //Checks who made the decision
            else if (lineArray[0].startsWith("decisionMaker:")) {
                if (lineArray[0].substring(14).equals("user")) {
                    //User assessed the set of scenarios until '-----'
                    processUserDecisions(logReader);
                } else {
                    //Algorithm assessed the set of scenarios until '-----'
                    processAlgorithmDecisions(logReader);
                }
            }
        }
    }    

    /**
     * Processes the user decided scenarios
     * @param logReader the scanner that is processing the log file
     */
    private void processUserDecisions(Scanner logReader) {
        boolean processDataSet = true;

        while (processDataSet && logReader.hasNextLine()) {
            String[] lineArray = logReader.nextLine().split(",", 8);

            // Makes a new scenario object and adds it to the scenarios arraylist
            if (lineArray[0].startsWith("scenario:")) {
                String disaster = lineArray[0].substring(9);
                userDecidedScenarios.add(new Scenario(disaster));
            } 
            // Adds a location to the previously added scenario
            else if (lineArray[0].startsWith("location:")) {
                String locData = lineArray[0].substring(9);
                userDecidedScenarios.get(userDecidedScenarios.size() - 1).addLocation(locData);
                //Determine if location characters have perished or not
                if (locData.split(";")[3].equals("saved")) {
                    userSavedLocations.add(userDecidedScenarios.get(userDecidedScenarios.size() - 1).getNumberOfLocations() - 1);
                }
            } 
            // Check if end of current dataset has been reached
            else if (lineArray[0].equals("-----")) {
                processDataSet = false;
            } 
            // All other chekcs have been done, so the characters can be added to the previously added location
            else {
                try {
                    userDecidedScenarios.get(userDecidedScenarios.size() - 1).addCharacter(lineArray);
                } catch (NumberFormatException | InvalidCharacteristicException e) {
                    // This should only ever be reached if the log file is corrupted
                    System.out.print("ERROR: LogFile is corrupted!");
                    System.exit(1);
                }
            }
        }
    }

     /**
     * Processes the algorithm decided scenarios
     * @param logReader the scanner that is processing the log file
     */
    private void processAlgorithmDecisions (Scanner logReader) {
        boolean processDataSet = true;

        while (processDataSet && logReader.hasNextLine()) {
            String[] lineArray = logReader.nextLine().split(",", 8);

            // Makes a new scenario object and adds it to the scenarios arraylist
            if (lineArray[0].startsWith("scenario:")) {
                String disaster = lineArray[0].substring(9);
                algorithmDecidedScenarios.add(new Scenario(disaster));
            } 
            // Adds a location to the previously added scenario
            else if (lineArray[0].startsWith("location:")) {
                String locData = lineArray[0].substring(9);
                algorithmDecidedScenarios.get(algorithmDecidedScenarios.size() - 1).addLocation(locData);
                //Determine if location characters have perished or not
                if (locData.split(";")[3].equals("saved")) {
                    algorithmSavedLocations.add(algorithmDecidedScenarios.get(algorithmDecidedScenarios.size() - 1).getNumberOfLocations() - 1);
                }
            } 
            // Check if end of current dataset has been reached
            else if (lineArray[0].equals("-----")) {
                processDataSet = false;
            } 
            // All other chekcs have been done, so the characters can be added to the previously added location
            else {
                try {
                    algorithmDecidedScenarios.get(algorithmDecidedScenarios.size() - 1).addCharacter(lineArray);
                } catch (NumberFormatException | InvalidCharacteristicException e) {
                    // This should only ever be reached if the log file is corrupted
                    System.out.print("ERROR: LogFile is corrupted!");
                    System.exit(1);
                }
            }
        }
    }
}
