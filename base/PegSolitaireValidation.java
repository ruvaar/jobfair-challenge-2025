package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PegSolitaireValidation {

    public static void main(String[] args) {

        String solverFileName = "BasicSolver";

        List<TestData> allTests = readTests("tests/public").stream().sorted(Comparator.comparing(TestData::getId)).toList();

        int successCount = 0;
        int failedCount = 0;

        try {
            Class<?> solverClass = Class.forName(getClassName(solverFileName));
            if (IPegSolitaireSolver.class.isAssignableFrom(solverClass)) {
                IPegSolitaireSolver instance = (IPegSolitaireSolver) solverClass.getDeclaredConstructor().newInstance();
                System.out.println("---------------- TESTS STARTED ----------------");
                for (TestData test : allTests) {
                    System.out.printf("[%s]", test.getId());
                    long startPosition = test.getStart();
                    long endPosition = test.getGoal();
                    long start = System.nanoTime();
                    long[] solution = instance.solve(startPosition, endPosition);
                    long executionTime = (System.nanoTime() - start) / 1_000_000;

                    try {
                        if (validateSolution(solution, startPosition, endPosition, test.isReachable(), true, executionTime)) {
                            successCount++;
                        } else {
                            failedCount++;
                        }
                    } catch (Exception e) {
                        failedCount++;
                    }
                }
            } else {
                System.out.println("Solution class does not implement IPegSolitaireSolver!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + getClassName(solverFileName));
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        System.out.println("---------------- TEST SUMMARY ----------------");
        System.out.println("Passed: " + successCount + "/" + allTests.size() + " tests.");
        System.out.println("Failed: " + failedCount + "/" + allTests.size() + " tests.");
        System.out.println("--------------- TESTS FINISHED ---------------");
    }

    public static String getClassName(String filename) {
        return "solver." + filename;
    }

    public static boolean validateSolution(long[] solution, long start, long end, boolean reachable, boolean log, long executionTime) {
        if (!reachable) {
            if (solution.length == 0) {
                logResult("+", 0, executionTime, null);
                return true;
            }
            if (log) logResult("-", 0, 0, "Test marked as unsolvable produced a result");
            return false;
        }
        if (start != solution[0]) {
            if (log) logResult("-", 0, 0, "Starting position of the board is not the same as defined");
            return false;
        }
        if (end != solution[solution.length - 1]) {
            if (log) logResult("-", 0, 0, "End position of the board is not the same as defined");
            return false;
        }
        for (int i = 0; i < solution.length - 1; i++) {
            if (!validateMove(solution[i], solution[i + 1], i, log)) {
                return false;
            }
        }
        logResult("+", solution.length - 1, executionTime, null);
        return true;
    }

    private static boolean validateMove(long before, long after, int i, boolean log) {
        long move = before ^ after;
        if (Long.bitCount(move) != 3) {
            if (log) logResult("-", 0, 0, "Invalid move: " + i + "(In a valid move exactly 3 positions are changed)");
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
            if (log) logResult("-", 0, 0, "Invalid move: " + i);
            return false;
        }
        if ((after & 438808218710499L) != 0) {
            if (log) logResult("-", 0, 0, "Invalid move: " + i + "(The move contains a peg on an invalid field)");
            return false;
        }
        return true;
    }

    public static List<TestData> readTests(String testsFolder) {
        List<TestData> testData = new ArrayList<>();
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
                    testData.add(new TestData(start, end, solveable != 0, file.getName().replaceAll("\\D+", "")
                    ));
                }
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format in the file: " + e.getMessage());
            }
        }
        return testData;
    }

    public static void logResult(String status, int steps, long executionTime, String errorMsg) {
        final String GREEN = "\u001B[32m";
        final String RED = "\u001B[31m";
        final String RESET = "\u001B[0m";

        String color = status.equals("+") ? GREEN : RED;

        if (errorMsg != null && !errorMsg.isEmpty()) {
            System.out.printf("%s%s[%s]%s[%s]%n",
                    ".".repeat(30),
                    color,
                    status,
                    RESET,
                    errorMsg
            );
        } else {
            System.out.printf("%s%s[%s]%s[steps: %d][%d ms]%n",
                    ".".repeat(30),
                    color,
                    status,
                    RESET,
                    steps,
                    executionTime
            );
        }
    }


}
