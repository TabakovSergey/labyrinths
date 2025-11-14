package academy.maze.IO;

import static org.assertj.core.api.Assertions.assertThat;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class MazeIOTest {

    @Test
    void loadFromFile_shouldParseGridIntoMaze() throws IOException {
        Path tmp = Files.createTempFile("maze-load", ".txt");
        Files.writeString(tmp, String.join(System.lineSeparator(), "###", "# #", "###"));

        Maze maze = MazeIO.loadFromFile(tmp.toFile());

        assertThat(maze.x()).isEqualTo(3);
        assertThat(maze.y()).isEqualTo(3);
        assertThat(maze.cells()[1][1]).isEqualTo(CellType.PATH);
        assertThat(maze.cells()[0][0]).isEqualTo(CellType.WALL);
    }

    @Test
    void saveToFile_shouldPersistWallsAndPathOverlay() throws IOException {
        CellType[][] cells = {
            {CellType.WALL, CellType.WALL, CellType.WALL},
            {CellType.WALL, CellType.PATH, CellType.WALL},
            {CellType.WALL, CellType.PATH, CellType.WALL}
        };
        Maze maze = new Maze(cells, 3, 3);
        academy.maze.dto.Path path = new academy.maze.dto.Path(new Point[] {new Point(1, 1), new Point(2, 1)});

        Path tmp = Files.createTempFile("maze-save", ".txt");
        MazeIO.saveToFile(maze, tmp.toFile(), path);

        String expected = String.join(System.lineSeparator(), "###", "#.#", "#.#") + System.lineSeparator();
        assertThat(Files.readString(tmp)).isEqualTo(expected);
    }
}
