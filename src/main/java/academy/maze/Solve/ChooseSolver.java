package academy.maze.Solve;

/**
 * Фабрика для выбора решателя лабиринтов по имени алгоритма.
 * 
 * <p>Поддерживаемые алгоритмы:
 * <ul>
 *   <li>"dijkstra" - алгоритм Дейкстры</li>
 *   <li>"astar" - алгоритм A*</li>
 * </ul>
 */
public class ChooseSolver {
    /**
     * Выбирает решатель лабиринта по имени алгоритма.
     * 
     * @param word имя алгоритма (регистр не важен)
     * @return экземпляр решателя или null, если алгоритм не найден
     */
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
