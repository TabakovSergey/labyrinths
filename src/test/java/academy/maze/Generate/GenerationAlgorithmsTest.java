package academy.maze.Generate;

import static org.assertj.core.api.Assertions.assertThat;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import org.junit.jupiter.api.Test;

class GenerationAlgorithmsTest {

    @Test
    void dfsGeneratorProducesOddGridWithBorderWalls() {
        Maze maze = new GenerationDFS().generate(2, 2);

        assertThat(maze.x()).isEqualTo(5);
        assertThat(maze.y()).isEqualTo(5);
        assertBordersAreWalls(maze);
        assertThat(maze.cells()[1][1]).isEqualTo(CellType.PATH);
    }

    @Test
    void primGeneratorProducesOddGridWithBorderWalls() {
        Maze maze = new GenerationPrima().generate(2, 2);

        assertThat(maze.x()).isEqualTo(5);
        assertThat(maze.y()).isEqualTo(5);
        assertBordersAreWalls(maze);
        assertThat(maze.cells()[1][1]).isEqualTo(CellType.PATH);
    }

    private static void assertBordersAreWalls(Maze maze) {
        CellType[][] cells = maze.cells();
        for (int c = 0; c < maze.y(); c++) {
            assertThat(cells[0][c]).isEqualTo(CellType.WALL);
            assertThat(cells[maze.x() - 1][c]).isEqualTo(CellType.WALL);
        }
        for (int r = 0; r < maze.x(); r++) {
            assertThat(cells[r][0]).isEqualTo(CellType.WALL);
            assertThat(cells[r][maze.y() - 1]).isEqualTo(CellType.WALL);
        }
    }
}
