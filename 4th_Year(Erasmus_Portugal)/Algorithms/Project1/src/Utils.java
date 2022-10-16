import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class Utils {

    public Utils(String fileName) {
        try {
            List<Championship> champions = readChampionshipsFromCSV(fileName);
        } catch (Exception e) {
            System.out.println("There's a problem!");
        }
    }

    static List<Championship> readChampionshipsFromCSV(String fileName) {
        List<Championship> fileChampions = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(fileName));
            sc.nextLine();
            sc.useDelimiter(",");
            List<String> generalInfo = new ArrayList<>();
            List<String> eachPosition = new ArrayList<>();
            String newLineString = "";
            boolean evenColumn = true;
            String currentString;
            while (sc.hasNext()) {
                if (newLineString.isEmpty()) {
                    currentString = sc.next();
                    if (currentString.contains("\r")) {
                        String[] splitted = currentString.split("\r");
                        if (splitted[1].length() > 2)
                            splitted[1] = splitted[1].substring(1); //remove \n\r
                        currentString = splitted[0];
                        newLineString = splitted[1];
//                    System.out.println(Arrays.toString(splitted));
                    }
                } else {
                    currentString = newLineString;
                    newLineString = "";
                    evenColumn = !evenColumn;
                }
                //FILL IN THE ARRAYS
                if (evenColumn) {
                    generalInfo.add(currentString);
                } else {
                    eachPosition.add(currentString);
                }
                //Fill up the next line with the right array
                if (!newLineString.isEmpty() || !sc.hasNext()) {
//                System.out.println("I entered here!");
//                System.out.println(generalInfo);
//                System.out.println(eachPosition);
                    if (!evenColumn) {
                        //CHAMPION LIST AND RACE FILL UP
                        Championship currentchmp = new Championship(Integer.parseInt(generalInfo.get(1)), generalInfo.get(0), generalInfo.get(2), generalInfo.get(3), Integer.parseInt(generalInfo.get(25).replaceAll("[^0-9]", "")), Integer.parseInt(generalInfo.get(26)));
                        for (int j = 0; j < 20; j++) {
                            String currentPosition;
                            boolean currentFinished = false;
                            if (Pattern.matches("\\d{1,2}", eachPosition.get(j + 4))) {
                                currentPosition = eachPosition.get(j + 4);
                                currentFinished = true;
                            } else currentPosition = "0";
                            //Race currentRace = new Race(generalInfo.get(j + 3), generalInfo.get(0), Integer.parseInt(generalInfo.get(1)), Integer.parseInt(currentPosition), currentFinished);
                            Race currentRace = new Race(generalInfo.get(j + 4), generalInfo.get(0), Integer.parseInt(generalInfo.get(1)), Integer.parseInt(currentPosition), currentFinished);
                            currentchmp.addRace(j + 1, currentRace);
                        }
                        fileChampions.add(currentchmp);
                        generalInfo.clear();
                        eachPosition.clear();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("There's a problem!");
        }
        return fileChampions;
    }

    static void sortChampionships(List<Championship> champs) {
        champs.sort(Championship::compareTo);
    }

    static void printChampionships(List<Championship> champ) {
        for (int i = 0; i < champ.size(); i++)
            System.out.println(champ.get(i).toString());
    }

    static List<Championship> filterByRacer(List<Championship> champs, String racer) {
        for (int i = 0; i < champs.size(); i++) {
            if (!champs.get(i).getRacer().equals(racer)) champs.remove(i);
        }
        return champs;
    }

    static List<Championship> filterByTeam(List<Championship> champs, String teamName, String category) {
        for (int i = 0; i < champs.size(); i++) {
            if (!champs.get(i).getCategory().equals(category)) champs.remove(champs.get(i));
        }
        sortChampionships(champs);
        return champs;
    }

    static void printRacerStats(List<Championship> champs, String racer) {
        int first = 0, second = 0, third = 0;
        for (int i = 0; i < champs.size(); i++) {
            if (champs.get(i).getRacer().equals(racer)) {
                for (int j = 1; j < 21; j++) {
                    switch (champs.get(i).getRace(j).getPosition()) {
                        case 1:
                            first++;
                            break;
                        case 2:
                            second++;
                            break;
                        case 3:
                            third++;
                    }
                }
            }
        }
        System.out.println("Racer: " + racer);
        System.out.println("1st: " + first + "\n2nd: " + second + "\n3rd: " + third);
    }

    static void printRacerCircuitRanking(List<Championship> champs, String racer) { //needs fixes
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < champs.size() && champs.get(i).getRacer().equals(racer); i++) {
            for (int j = 1; j < 21; j++) {
                //define current position and circuit
                int currentPosition;
                if (champs.get(i).getRace(j).getPosition() > 1 && champs.get(i).getRace(j).getPosition() < 11) {
                    currentPosition = (int) Math.pow(2, Math.abs(champs.get(i).getRace(j).getPosition() - 11));
                    String currentCircuit = champs.get(i).getRace(j).getCircuit();
                    if (map.containsKey(currentCircuit))
                        map.put(currentCircuit, map.get(currentCircuit) + currentPosition);
                    else
                        map.put(currentCircuit, currentPosition);
                }
            }
        }
        //Print the map
        map.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
    }

    static List<Race> getBestRaces(List<Championship> champs, String racer) {
        return null;
    }

    static List<String> getTeams(List<Championship> champs, String category) {
        List<String> team = new ArrayList<>();
        for (int i = 0; i < champs.size(); i++) {
            if (champs.get(i).getCategory().equals(category)) {
                if (!team.contains(champs.get(i).getCategory()))
                    team.add(champs.get(i).getCategory());
            }
        }
        return team;
    }
}