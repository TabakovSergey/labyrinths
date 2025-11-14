package academy.maze.Solve;

import static org.assertj.core.api.Assertions.assertThat;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.util.MazeTestFactory;
import org.junit.jupiter.api.Test;

class SolverAlgorithmsTest {

    private static final Maze SIMPLE_MAZE = MazeTestFactory.fromStringGrid("#####", "#   #", "### #", "#   #", "#####");

    @Test
    void dijkstraShouldReturnShortestPath() {
        Solver solver = new SolverDijkstra();

        Path path = solver.solve(SIMPLE_MAZE, new Point(1, 1), new Point(3, 3));

        assertThat(path.points())
                .containsExactly(new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(2, 3), new Point(3, 3));
    }

    @Test
    void dijkstraShouldReturnEmptyPathWhenBlocked() {
        Maze blocked = MazeTestFactory.fromStringGrid("#####", "#   #", "#####", "#   #", "#####");
        Solver solver = new SolverDijkstra();

        Path path = solver.solve(blocked, new Point(1, 1), new Point(3, 3));

        assertThat(path.points()).isEmpty();
    }

    @Test
    void aStarShouldFindSamePath() {
        Solver solver = new SolverAStar();

        Path path = solver.solve(SIMPLE_MAZE, new Point(1, 1), new Point(3, 3));

        assertThat(path.points())
                .containsExactly(new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(2, 3), new Point(3, 3));
    }
}
