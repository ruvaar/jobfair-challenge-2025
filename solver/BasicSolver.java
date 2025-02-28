package solver;

import base.IPegSolitaireSolver;

import java.util.ArrayList;
import java.util.List;

public class BasicSolver implements IPegSolitaireSolver {

    private static final List<Integer> OFFSETS = List.of(1, -1, 7, -7); // left, right, down, up

    // a simple and naive solver that each time takes a random move out of all possible valid moves
    // if there are no available valid moves, reset and start again
    @Override
    public long[] solve(long initialBoard, long goalBoard) {
        List<Long> solutionSteps = new ArrayList<>();
        solutionSteps.add(initialBoard);

        int numberOfFinalPegs = (int) (Math.log(goalBoard) / Math.log(2)) + 1;

        long currentBoard = initialBoard;
        long limit = 1_000_000_000L;
        long attempts = 0;

        while (currentBoard != goalBoard){
            List<Long> allValidMoves = getAllValidMoves(currentBoard);
            int numberOfPegs = (int) (Math.log(currentBoard) / Math.log(2)) + 1;
            if (allValidMoves.isEmpty() || numberOfPegs < numberOfFinalPegs) {
                attempts++;
                if (attempts > limit) {
                    break;
                }
                solutionSteps.clear();
                solutionSteps.add(initialBoard);
                currentBoard = initialBoard;
                continue;
            }

            int randomIndex = (int) (Math.random() * allValidMoves.size());
            currentBoard = allValidMoves.get(randomIndex);
            solutionSteps.add(currentBoard);
        }

        return solutionSteps.stream().mapToLong(Long::longValue).toArray();
    }

    private List<Long> getAllValidMoves(long state) {
        List<Long> validMoves = new ArrayList<>();

        for (int i = 0; i < 49; i++) {
            if ((state & (1L << i)) != 0) {
                // try a move if it is valid
                for (int offset : OFFSETS) {
                    long move = move(state, i, offset);
                    if (move != -1) {
                        validMoves.add(move);
                    }
                }
            }
        }
        return validMoves;
    }

    private long move(long currentBoard, int position, int offset) {
        int jumpedPos = position + offset; // Position of the jumped peg
        int landingPos = position + 2 * offset; // Position where the peg lands

        // If move goes out of bounds
        if (jumpedPos < 0 || jumpedPos >= 64 || landingPos < 0 || landingPos >= 64) {
            return -1;
        }

        // Check if there's a peg at the starting position
        if ((currentBoard & (1L << position)) == 0) {
            return -1;
        }

        // Check if there's a peg at the jumped position
        if ((currentBoard & (1L << jumpedPos)) == 0) {
            return -1;
        }

        // Check if the landing position is empty
        if ((currentBoard & (1L << landingPos)) != 0) {
            return -1;
        }

        // Perform the move: remove starting peg, remove jumped peg, add landing peg
        currentBoard &= ~(1L << position); // Remove starting peg
        currentBoard &= ~(1L << jumpedPos); // Remove jumped peg
        currentBoard |= (1L << landingPos); // Add landing peg

        // if a peg is on an invalid field
        if ((currentBoard & 438808218710499L) != 0) {
            return -1L;
        }

        return currentBoard;

    }

    @Override
    public String[] personalData() {
        return new String[] {"Priden", "Študent"};
    }

}
