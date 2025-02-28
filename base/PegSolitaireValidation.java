package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PegSolitaireValidation {

    public static void main(String[] args) {

        String solverFileName = "BasicSolver";

        List<base.TestData> allTests = readTests("tests/public");
        int testIndex = 0;
        try {
            Class<?> solverClass = Class.forName(getClassName(solverFileName));
            if (base.IPegSolitaireSolver.class.isAssignableFrom(solverClass)) {
                base.IPegSolitaireSolver instance = (base.IPegSolitaireSolver) solverClass.getDeclaredConstructor().newInstance();
                for (base.TestData test : allTests) {
                    System.out.println("---------------- TEST " + ++testIndex + " ----------------");
                    long startPosition = test.getStart();
                    long endPosition = test.getGoal();
                    long start = System.nanoTime();
                    long[] solution = instance.solve(startPosition, endPosition);
                    long executionTime = (System.nanoTime() - start) / 1_000_000;
                    System.out.println("Execution time: " + executionTime + " ms");
                    if (validateSolution(solution, startPosition, endPosition, test.isReachable())) {
                        System.out.println("Solution is valid! Solver found solution in " + solution.length + " steps.");
                    }
                }
            } else {
                System.out.println("Solution class does not implement IPegSolitaireSolver!");
            }
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + getClassName(solverFileName));
        }
        catch (Exception e) {
            System.out.println("There was an error during calculation:" + e.getMessage());
        }
    }

    public static String getClassName(String filename) {
        return "solver." + filename;
    }

    public static boolean validateSolution(long[] solution, long start, long end, boolean reachable) {
        if (!reachable) {
            System.out.println("test marked as unreachable - checking if array is empty");
            if (solution.length == 0) {
                return true;
            }
            System.out.println("test marked as unreachable produced a result.");
            return false;
        }
        if (start != solution[0]) {
            System.out.println("starting position of the board is not the same as defined");
            return false;
        }
        if (end != solution[solution.length - 1]) {
            System.out.println("end position of the board is not the same as defined");
            return false;
        }
        for (int i = 0; i < solution.length - 1; i++) {
            if (!validateMove(solution[i], solution[i + 1])) {
                System.out.println("move " + i + " is not valid");
                return false;
            }
        }
        return true;
    }

    private static boolean validateMove(long before, long after) {
        long move = before ^ after;
        if (Long.bitCount(move) != 3) {
            System.out.println("In a valid move exactly 3 positions are changed - invalid move");
            return false;
        }
        long positionBefore = before & move;
        long positionAfter = after & move;
        long moveDown = (positionBefore >> 7) - positionAfter;
        long moveUp = (positionBefore << 7) - positionAfter;
        long moveLeft = (positionBefore << 1) - positionAfter;
        long moveRight = (positionBefore >> 1) - positionAfter;
        if ((moveDown >> 7) - positionAfter != 0 &&
                (moveUp << 7) - positionAfter != 0 &&
                (moveLeft << 1) - positionAfter != 0 &&
                (moveRight >> 1) - positionAfter != 0) {
            System.out.println("The move is invalid");
            return false;
        }
        if ((after & 438808218710499L) != 0) {
            System.out.println("The move contains a peg on an invalid field");
            return false;
        }
        return true;
    }

    public static List<base.TestData> readTests(String testsFolder) {
        List<base.TestData> testData = new ArrayList<>();
        File folder = new File(testsFolder);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Invalid folder path. Please provide a valid directory!");
            return testData;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("No .txt files found in the folder.");
            return testData;
        }

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                if (folder.exists() && folder.isDirectory()) {
                    String line;
                    line = reader.readLine();
                    long solveable = Long.parseLong(line);
                    line = reader.readLine();
                    long start = Long.parseLong(line);
                    line = reader.readLine();
                    long end = Long.parseLong(line);
                    testData.add(new base.TestData(start, end, solveable != 0));
                }
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format in the file: " + e.getMessage());
            }
        }
        return testData;
    }

}
