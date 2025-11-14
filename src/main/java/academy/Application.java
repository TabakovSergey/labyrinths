package academy;

import academy.maze.Generate.ChooseGenerator;
import academy.maze.Generate.Generator;
import academy.maze.IO.MazeIO;
import academy.maze.Solve.ChooseSolver;
import academy.maze.Solve.Solver;
import academy.maze.View.MazePrinter;
import academy.maze.View.MazeWindow;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

@Command(name = "Application Example", version = "Example 1.0", mixinStandardHelpOptions = true,
        subcommands = {Application.GenerateCommand.class, Application.SolveCommand.class})
public class Application implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final ObjectReader YAML_READER =
            new ObjectMapper(new YAMLFactory()).findAndRegisterModules().reader();

    @Command(
        name = "generate",
        description = "Generate a new Maze"
    )
    static class GenerateCommand implements Runnable{
        @Option(
            names = {"--width"},
            description = "Number of cells horizontally (maze width)",
            required = true
        )
        private int width;

        @Option(
            names = {"--height"},
            description = "Number of cells vertically (maze height)",
            required = true
        )
        private int height;

        @Option(
            names = {"--window"},
            description = "whether to show the graphics window or not",
            defaultValue = "no"
        )
        private String windowMode = "no";
        @Option(
            names = {"--algorithm"},
            description = "Choose algorithm for build Maze",
            required = true
        )
        private String algorithm;

        @Option(
            names = {"--output", "-o"},
            description = "Output file to save Maze"
        )
        private File output;

        public void run() {
            validateDimensions(width, height);
            System.out.println("Generating Maze, algorithm: " + algorithm);

            Generator gen = ChooseGenerator.choose(algorithm);
            if (gen == null) {
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
            }
            Maze res = gen.generate(width, height);
            MazePrinter.printMazeNotPath(res);

            showWindowIfRequested(windowMode, res, null);

            if(output != null) {
                MazeIO.saveToFile(res, output, null);
            }
        }
    }


    @Command(
        name = "solve",
        description = "Solve an Maze"
    )
    static class SolveCommand implements Runnable{

        @Option(
            names = {"--window"},
            description = "whether to show the graphics window or not",
            defaultValue = "no"
        )
        private String windowMode = "no";
        @Option(
            names = {"--algorithm"},
            description = "Choose algorithm for build Maze",
            required = true
        )
        private String algorithm;

        @Option(
            names = {"--start"},
            description = "Write start point for Maze",
            required = true
        )
        private String start;

        @Option(
            names = {"--end"},
            description = "Write end point for Maze",
            required = true
        )
        private String end;

        @Option(
            names = {"--input", "-i"},
            description = "input file to save Maze",
            required = true
        )
        private File input;

        @Option(
            names = {"--output"},
            description = "Output file to save Maze"
        )
        private File output;
        public void run() {
            System.out.println("Solving Maze, algorithm: " + algorithm);

            Maze maze = MazeIO.loadFromFile(input);

            Point startPoint = parse(start);
            Point endPoint = parse(end);

            if(!inBounds(startPoint, maze) || !inBounds(endPoint, maze))
            {
                System.out.println("Start or end point is out of maze bounds");
                return;
            }

            if(!isPathCell(startPoint, maze) || !isPathCell(endPoint, maze)) {
                System.out.println("Start or end point is inside a wall");
                return;
            }

            Solver solver = ChooseSolver.choose(algorithm);
            if(solver == null)
            {
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
            }
            Path path = solver.solve(maze, startPoint, endPoint);
            if (path == null || path.points().length == 0) {
                System.out.println("Path was not found.");
                JOptionPane.showMessageDialog(
                    null,
                    "Path was not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            MazePrinter.printMazePath(maze, path);

            showWindowIfRequested(windowMode, maze, path);

            if (output != null) {
                MazeIO.saveToFile(maze, output, path);
            }

        }
        private Point parse(String s)
        {
            if (s == null) {
                throw new IllegalArgumentException("Point must be provided in format x,y");
            }
            String[] parts = s.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Point must be provided in format x,y");
            }
            try {
                int px = Integer.parseInt(parts[0].trim());
                int py = Integer.parseInt(parts[1].trim());
                return new Point(px, py);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Point must contain integer coordinates", e);
            }

        }
        private boolean inBounds(Point p, Maze maze)
        {
            return p.x() >= 0 && p.x() < maze.x() && p.y() >= 0 && p.y() < maze.y();
        }

        private boolean isPathCell(Point p, Maze maze) {
            return maze.cells()[p.x()][p.y()] == CellType.PATH;
        }
    }



    @Option(
            names = {"-s", "--font-size"},
            description = "Font size")
    int fontSize;

    @Parameters(
            paramLabel = "<word>",
            defaultValue = "Hello, picocli",
            description = "Words to be translated into ASCII art.")
    private String[] words;



    @Option(
            names = {"-c", "--config"},
            description = "Path to JSON config file")
    private File configPath;



    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);

        //System.exit(exitCode);
    }

    @Override
    public void run() {
        var config = loadConfig();
        LOGGER.atInfo().addKeyValue("config", config).log("Config content");
        Generator gen;
    }

    private static void validateDimensions(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive integers.");
        }
    }

    private static void showWindowIfRequested(String windowMode, Maze maze, Path path) {
        if ("yes".equalsIgnoreCase(windowMode)) {
            SwingUtilities.invokeLater(() -> new MazeWindow(maze, path));
        }
    }


    private AppConfig loadConfig() {
        // fill with cli options
        if (configPath == null) return new AppConfig(fontSize, words);

        // use config file if provided
        try {
            return YAML_READER.readValue(configPath, AppConfig.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
