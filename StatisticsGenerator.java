import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import lib.LivingBeing;

/**
 * A class use to generate the statistics for judgment or running a simulation
 * @author Joshia Nambi
 */
public class StatisticsGenerator {

    private ArrayList<Pair> statistics;
    private int scenarioCount;
    private Pair averageAge;
    
    public StatisticsGenerator() {
        statistics = new ArrayList<Pair>();
        averageAge = new Pair("age");
        scenarioCount = 0;
    }

    /**
     * Print the statistics to the console, avoiding some predefined attributes
     */
    public void printStatistics(String statisticType) {
        System.out.println("======================================");
        System.out.println("# " + statisticType);
        System.out.println("======================================");
        System.out.println("- % SAVED AFTER " + scenarioCount + " RUNS");
        Collections.sort(statistics, (new PairComparator()));
        for (Pair pair : statistics) {
            switch (pair.getAttribute()) {
                case "non-pregnant":
                case "unknown":
                case "unspecified":
                case "none":
                case "non-pet":
                    break;
            
                default:
                    System.out.printf("%s: %.2f\n", pair.getAttribute(), pair.getSurvivalRatio());
                    break;
            }
        }
        System.out.println("--");
        System.out.printf("average age: %.2f\n", averageAge.getSurvivalRatio()); 
    }

    /**
     * Parses a current scenario to add to the statistics
     * @param scenario current dilemma
     * @param deployedLocation when the rescuebot was chosen to be deployed
     */
    public void parseScenarioForStatistics(Scenario scenario, int deployedLocation) {
        scenarioCount++;
        for (int i = 0; i < scenario.getNumberOfLocations(); i++) {
            //For each location
            Location currLoc = scenario.getLocation(i);
            for (int j = 0; j < currLoc.getNumberOfCharacters(); j++) {
                //For each character in the location
                String[] characterRepresentation = currLoc.getCharacter(j).getArrayRepresentation();
                if (characterRepresentation[0].equals("human")) {
                    processHumanStatistics(characterRepresentation, i == deployedLocation);
                } else {
                    processAnimalStatistics(characterRepresentation, i == deployedLocation);
                }
                processTresspassingStatus(currLoc.getIllegalEntry(), i == deployedLocation);
            }
        }
    }

    /**
     * Processes all the characters to determine how many tresspassed/legal access of surviving characters
     * @param trespassing if location was tresspassed
     * @param survived if character survived
     */
    private void processTresspassingStatus(boolean trespassing, boolean survived) {
        if (trespassing) {
            String attribute = "trespassing";
            int attributeIndex = statisticAttributeSearch(attribute);
            if (attributeIndex >= 0) {
                //Attribute already exists in the statistics
                if (survived) {
                    statistics.get(attributeIndex).addSavedAttribute();
                } else {
                    statistics.get(attributeIndex).addPerishedAttribute();
                }
            } else {
                //Create new attrivute as it doesn't exist
                statistics.add(new Pair(attribute));
                if (survived) {
                    statistics.get(statistics.size()-1).addSavedAttribute();
                } else {
                    statistics.get(statistics.size()-1).addPerishedAttribute();
                }
            }
        } else {
            String attribute = "legal";
            int attributeIndex = statisticAttributeSearch(attribute);
            if (attributeIndex >= 0) {
                //Attribute already exists in the statistics
                if (survived) {
                    statistics.get(attributeIndex).addSavedAttribute();
                } else {
                    statistics.get(attributeIndex).addPerishedAttribute();
                }
            } else {
                //Create new attrivute as it doesn't exist
                statistics.add(new Pair(attribute));
                if (survived) {
                    statistics.get(statistics.size()-1).addSavedAttribute();
                } else {
                    statistics.get(statistics.size()-1).addPerishedAttribute();
                }
            }
        }
    }

    /**
     * Processes statistics for an Animal
     * @param animalRepresentation String representation of the animal
     * @param survived if the animal survived
     */
    private void processAnimalStatistics(String[] animalRepresentation, boolean survived) {
        for (int i = 0; i < animalRepresentation.length; i++) {
            String attribute = animalRepresentation[i];
            int attributeIndex = statisticAttributeSearch(attribute);
            if (attributeIndex >= 0) {
                //Attribute already exists in the statistics
                if (survived) {
                    statistics.get(attributeIndex).addSavedAttribute();
                } else {
                    statistics.get(attributeIndex).addPerishedAttribute();
                }
            } else {
                //Create new attrivute as it doesn't exist
                statistics.add(new Pair(attribute));
                if (survived) {
                    statistics.get(statistics.size()-1).addSavedAttribute();
                } else {
                    statistics.get(statistics.size()-1).addPerishedAttribute();
                }
            }
        }
    }

    /**
     * Processes the statistics for a human
     * @param humanRepresentation String representation of the human
     * @param survived if the human survived
     */
    private void processHumanStatistics(String[] humanRepresentation, boolean survived) {
        for (int i = 0; i < humanRepresentation.length; i++) {
            String attribute = humanRepresentation[i];
            //Process the age of the human
            if (i == 2) {
                if (!survived) {
                    continue;
                }
                int age = Integer.valueOf(attribute);
                averageAge.sumSaved(age);
                averageAge.addPerishedAttribute();
                continue;
            }
            int attributeIndex = statisticAttributeSearch(attribute);
            if (attributeIndex >= 0) {
                //Making sure that the attribute already exists in the statistics
                if (survived) {
                    statistics.get(attributeIndex).addSavedAttribute();
                } else {
                    statistics.get(attributeIndex).addPerishedAttribute();
                }
            } else {
                //Have to create the attribute as it doesn't exist
                statistics.add(new Pair(attribute));
                if (survived) {
                    statistics.get(statistics.size()-1).addSavedAttribute();
                } else {
                    statistics.get(statistics.size()-1).addPerishedAttribute();    
                }
            }
        }
    }    

    /**
     * Determine if the attribute has already been seen 
     * @param str the attribute
     * @return index if found, otherwise -1
     */
    private int statisticAttributeSearch(String str) {
        int indexFound = -1;
        for (int i = 0; i < statistics.size(); i++) {
            if (statistics.get(i).getAttribute().equals(str)) {
                indexFound = i;
            }
        }
        return indexFound;
    }

    /**
     * Saves the statistics to a logfile
     * @param logFilePath logfile to save to
     */
    public void saveStatisticsToLogFile(String logFilePath, ArrayList<Scenario> scenarios, ArrayList<Integer> savedLocations, String decisionMaker) {
        try {
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                generateLogFile(logFile);
            }
            PrintWriter statLogger = new PrintWriter(new FileOutputStream(logFile, true));
            statLogger.println("decisionMaker:" + decisionMaker);
            //For each scenario
            for (int i = 0; i < scenarios.size(); i++) {
                Scenario currentScenario = scenarios.get(i);
                statLogger.println("scenario:" + currentScenario.getDisaster() + ",,,,,,,");

                //For each location in the scenarion
                for (int j = 0; j < scenarios.get(i).getNumberOfLocations(); j++) {
                    Location currentLocation = currentScenario.getLocation(j);
                    String ifSavedString = ";" + ((savedLocations.get(i) == j) ? "saved" : "perished");
                    String trespassingString = ";" + ((currentLocation.getIllegalEntry()) ? "trespassing" : "legal");
                    statLogger.println("location:" + currentLocation.getLatitude() + ";" + currentLocation.getLongitude() + trespassingString + ifSavedString);
                    //For each character
                    for (int k = 0; k < currentLocation.getNumberOfCharacters(); k++) {
                        LivingBeing currCharacter = currentLocation.getCharacter(k);
                        statLogger.println(currCharacter.getCSVFormat());
                    }
                }
            }
            statLogger.println("-----");
            statLogger.close();                
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: could not print results. Target directory does not exist.");
            System.exit(1);
        }
    }

    /**
     * Generates a log file if it doesnt exist
     * @param logFile
     * @throws FileNotFoundException
     */
    private void generateLogFile(File logFile) throws FileNotFoundException {
        PrintWriter fileGenerator = new PrintWriter(logFile);
        fileGenerator.println(">>RescueBot Log File<<,gender,age,bodyType,profession,pregnant,species,isPet");
        fileGenerator.close();
    }
}
