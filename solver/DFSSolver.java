package solver;

import base.IPegSolitaireSolver;

import java.util.*;


public class DFSSolver implements IPegSolitaireSolver {


    private static final int SIZE = 7;
    private static final int BOARD_SIZE = 49; 

    // Invalid fields bitmask for standard English board shape
    private static final long INVALID_FIELDS_MASK = 438808218710499L;
    
    private static final class Move {
        final long fromMask;
        final long jumpedMask;
        final long toMask;

        Move(long f, long j, long t) {
            this.fromMask = f;
            this.jumpedMask = j;
            this.toMask = t;
        }
    }

    // Precompute all valid moves
    private static final List<Move> ALL_MOVES = buildMoveTable();

    private static List<Move> buildMoveTable() {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int pos = r * SIZE + c;

                // Right
                if (c + 2 < SIZE) {
                    int jumped = pos + 1;
                    int landing = pos + 2;
                    long fm = 1L << pos, jm = 1L << jumped, tm = 1L << landing;
                    if (((fm | jm | tm) & INVALID_FIELDS_MASK) == 0) {
                        moves.add(new Move(fm, jm, tm));
                    }
                }
                // Left
                if (c - 2 >= 0) {
                    int jumped = pos - 1;
                    int landing = pos - 2;
                    long fm = 1L << pos, jm = 1L << jumped, tm = 1L << landing;
                    if (((fm | jm | tm) & INVALID_FIELDS_MASK) == 0) {
                        moves.add(new Move(fm, jm, tm));
                    }
                }
                // Down
                if (r + 2 < SIZE) {
                    int jumped = (r + 1) * SIZE + c;
                    int landing = (r + 2) * SIZE + c;
                    long fm = 1L << pos, jm = 1L << jumped, tm = 1L << landing;
                    if (((fm | jm | tm) & INVALID_FIELDS_MASK) == 0) {
                        moves.add(new Move(fm, jm, tm));
                    }
                }
                // Up
                if (r - 2 >= 0) {
                    int jumped = (r - 1) * SIZE + c;
                    int landing = (r - 2) * SIZE + c;
                    long fm = 1L << pos, jm = 1L << jumped, tm = 1L << landing;
                    if (((fm | jm | tm) & INVALID_FIELDS_MASK) == 0) {
                        moves.add(new Move(fm, jm, tm));
                    }
                }
            }
        }
        return moves;
    }


    private static final int[] ROTATE90_INDEX   = new int[BOARD_SIZE];
    private static final int[] ROTATE180_INDEX  = new int[BOARD_SIZE];
    private static final int[] ROTATE270_INDEX  = new int[BOARD_SIZE];
    private static final int[] FLIP_H_INDEX     = new int[BOARD_SIZE];
    private static final int[] FLIP_V_INDEX     = new int[BOARD_SIZE];
    private static final int[] FLIP_D_INDEX     = new int[BOARD_SIZE];  
    private static final int[] FLIP_D2_INDEX    = new int[BOARD_SIZE];  

    static {
        buildSymmetryIndexes();
    }

    private static void buildSymmetryIndexes() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int i = r * SIZE + c;

                // For rotate90: (r,c) -> (c, 6-r)
                int r90r = c;
                int r90c = SIZE - 1 - r;
                ROTATE90_INDEX[i] = r90r * SIZE + r90c;

                // rotate180: (r,c) -> (6-r, 6-c)
                int r180r = SIZE - 1 - r;
                int r180c = SIZE - 1 - c;
                ROTATE180_INDEX[i] = r180r * SIZE + r180c;

                // rotate270: (r,c) -> (6-c, r)
                int r270r = SIZE - 1 - c;
                int r270c = r;
                ROTATE270_INDEX[i] = r270r * SIZE + r270c;

                // flipH (horizontal): (r,c) -> (r, 6-c)
                int fhc = SIZE - 1 - c;
                FLIP_H_INDEX[i] = r * SIZE + fhc;

                // flipV (vertical): (r,c) -> (6-r, c)
                int fvr = SIZE - 1 - r;
                FLIP_V_INDEX[i] = fvr * SIZE + c;

                // flipD (main diagonal): (r,c) -> (c, r)
                FLIP_D_INDEX[i] = c * SIZE + r;

                // flipD2 (secondary diagonal): (r,c) -> (6-c, 6-r)
                int fd2r = SIZE - 1 - c;
                int fd2c = SIZE - 1 - r;
                FLIP_D2_INDEX[i] = fd2r * SIZE + fd2c;
            }
        }
    }

    // Transformed board
    private static long transformBoard(long board, int[] transformIndex) {
        long result = 0L;
        for (int i = 0; i < BOARD_SIZE; i++) {
            // if bit i is set in 'board'
            if ((board & (1L << i)) != 0) {
                int j = transformIndex[i];
                // set bit j in result
                result |= (1L << j);
            }
        }
        return result;
    }

    // Return minimal bitboard (to avoid duplicates)
    private static long getCanonicalSymmetry(long board) {
        long best = board;

        long r90  = transformBoard(board, ROTATE90_INDEX);
        long r180 = transformBoard(board, ROTATE180_INDEX);
        long r270 = transformBoard(board, ROTATE270_INDEX);
        long fH   = transformBoard(board, FLIP_H_INDEX);
        long fV   = transformBoard(board, FLIP_V_INDEX);
        long fD   = transformBoard(board, FLIP_D_INDEX);
        long fD2  = transformBoard(board, FLIP_D2_INDEX);

        best = Math.min(best, r90);
        best = Math.min(best, r180);
        best = Math.min(best, r270);
        best = Math.min(best, fH);
        best = Math.min(best, fV);
        best = Math.min(best, fD);
        best = Math.min(best, fD2);

        return best;
    }




    
    private Set<Long> visited;
    private List<Long> solutionPath;

    @Override
    public long[] solve(long initialBoard, long goalBoard) {
        visited = new HashSet<>();
        solutionPath = new ArrayList<>();

        int startCount = Long.bitCount(initialBoard);
        int goalCount = Long.bitCount(goalBoard);
        // max depth ==> difference in peg count
        int maxDepth = startCount - goalCount;

        // impossible
        if (maxDepth < 0) {
            return new long[0];
        }
        // already solved
        if (initialBoard == goalBoard) {
            return new long[] { initialBoard };
        }

        List<Long> path = new ArrayList<>();
        path.add(initialBoard);

        boolean found = dfs(initialBoard, goalBoard, path, 0, maxDepth);
        if (found) {
            // convert List<Long> to long[]
            return solutionPath.stream().mapToLong(Long::longValue).toArray();
        } else {
            return new long[0]; // no solution
        }
    }

    private boolean dfs(long current, long goal, List<Long> path, int depth, int maxDepth) {
        
        if (current == goal) {
            solutionPath = new ArrayList<>(path);
            return true;
        }

        if (depth == maxDepth) {
            return false;
        }

        // Symmetry prune
        long canonical = getCanonicalSymmetry(current);
        if (visited.contains(canonical)) {
            return false;
        }
        visited.add(canonical);

        // Generate next states via precomputed moves
        for (Move mv : ALL_MOVES) {
            // Check if its a valid move (there has to be a peg at from and jumped but to has to be clear)
            if (((current & mv.fromMask) != 0) 
             && ((current & mv.jumpedMask) != 0)
             && ((current & mv.toMask) == 0)) 
            {
                // remove peg from 'from' and 'jumped' positions, add peg to 'to' position
                long nextBoard = current;
                nextBoard &= ~mv.fromMask;
                nextBoard &= ~mv.jumpedMask;
                nextBoard |= mv.toMask;

                // peg gets placed outside the board ==> invalid move
                if ((nextBoard & INVALID_FIELDS_MASK) != 0) {
                    continue;
                }

                path.add(nextBoard);
                if (dfs(nextBoard, goal, path, depth + 1, maxDepth)) {
                    return true;
                }
                path.remove(path.size() - 1);
            }
        }
        return false;
    }

    @Override
    public String[] personalData() {
        return new String[] {"Rudi", "Arcon"};
    }
}