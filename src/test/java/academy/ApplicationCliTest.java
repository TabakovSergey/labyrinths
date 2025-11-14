package academy;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class ApplicationCliTest {

    @Test
    void generateCommandShouldWriteMazeToFile() throws IOException {
        Path output = Files.createTempFile("maze-generated", ".txt");

        int exitCode = new CommandLine(new Application())
                .execute(
                        "generate",
                        "--algorithm",
                        "dfs",
                        "--width",
                        "1",
                        "--height",
                        "1",
                        "--window",
                        "no",
                        "--output",
                        output.toString());

        assertThat(exitCode).isEqualTo(0);
        String expected = String.join(System.lineSeparator(), "###", "# #", "###") + System.lineSeparator();
        assertThat(Files.readString(output)).isEqualTo(expected);
    }

    @Test
    void solveCommandShouldPersistSolvedMaze() throws IOException {
        Path input = Files.createTempFile("maze-input", ".txt");
        Files.writeString(input, String.join(System.lineSeparator(), "#####", "#   #", "### #", "#   #", "#####"));
        Path output = Files.createTempFile("maze-output", ".txt");

        int exitCode = new CommandLine(new Application())
                .execute(
                        "solve",
                        "--window",
                        "no",
                        "--algorithm",
                        "dijkstra",
                        "--start",
                        "1,1",
                        "--end",
                        "3,3",
                        "--input",
                        input.toString(),
                        "--output",
                        output.toString());

        assertThat(exitCode).isEqualTo(0);
        String expected = String.join(System.lineSeparator(), "#####", "#...#", "###.#", "#  .#", "#####")
                + System.lineSeparator();
        assertThat(Files.readString(output)).isEqualTo(expected);
    }
}
