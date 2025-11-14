package academy.maze.View;

import static org.assertj.core.api.Assertions.assertThat;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.util.MazeTestFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MazePrinterTest {

    @Test
    void printMazeWithoutPathShouldRenderWallsAndSpaces() {
        Maze maze = MazeTestFactory.fromStringGrid(
                "###",
                "# #",
                "###"
        );

        String output = captureOutput(() -> MazePrinter.printMazeNotPath(maze));

        String expected = String.join(System.lineSeparator(), "###", "# #", "###") + System.lineSeparator();
        assertThat(output).isEqualTo(expected);
    }

    @Test
    void printMazeWithPathShouldOverlayDots() {
        Maze maze = MazeTestFactory.fromStringGrid(
                "###",
                "# #",
                "###"
        );
        Path path = new Path(new Point[] {new Point(1, 1)});

        String output = captureOutput(() -> MazePrinter.printMazePath(maze, path));

        String expected = String.join(System.lineSeparator(), "###", "#.#", "###") + System.lineSeparator();
        assertThat(output).isEqualTo(expected);
    }

    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return out.toString(StandardCharsets.UTF_8);
    }
}
