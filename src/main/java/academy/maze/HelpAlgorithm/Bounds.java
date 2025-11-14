package academy.maze.HelpAlgorithm;

public class Bounds {
    public static boolean inBounds(int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }
}
