package base;

public interface IPegSolitaireSolver {

    /**
     * solver method that is called when checking your solution. Implement your solver here
     * @param initialBoard initial state of the board
     * @param goalBoard final state of the board
     * @return array of numbers, representing board states after each move
     */
    long[] solve(long initialBoard, long goalBoard);

    /**
     * method to give your name and surname as a String array
     * @return string array of your name and surname
     */
    String[] personalData();

}
