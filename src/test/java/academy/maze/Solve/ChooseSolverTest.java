package academy.maze.Solve;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ChooseSolverTest {

    @Test
    void chooseShouldReturnDijkstra() {
        assertThat(ChooseSolver.choose("dijkstra")).isInstanceOf(SolverDijkstra.class);
    }

    @Test
    void chooseShouldReturnAStar() {
        assertThat(ChooseSolver.choose("astar")).isInstanceOf(SolverAStar.class);
    }

    @Test
    void chooseShouldReturnNullForUnknownAlgorithm() {
        assertThat(ChooseSolver.choose("unknown")).isNull();
    }
}
