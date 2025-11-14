package academy.maze.Solve;

public class ChooseSolver {
    public static Solver choose(String word) {
        if (word == null) {
            return null;
        }
        return switch (word.toLowerCase()) {
            case "dijkstra" -> new SolverDijkstra();
            case "astar" -> new SolverAStar();
            default -> null;
        };
    }
}
