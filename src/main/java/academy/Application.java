package academy;

import academy.maze.Generate.ChooseGenerator;
import academy.maze.Generate.Generator;
import academy.maze.IO.MazeIO;
import academy.maze.Solve.ChooseSolver;
import academy.maze.Solve.Solver;
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
import java.nio.file.Files;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Главный класс приложения для генерации и решения лабиринтов.
 * 
 * <p>Приложение предоставляет CLI интерфейс для работы с лабиринтами:
 * <ul>
 *   <li>Генерация лабиринтов с использованием различных алгоритмов (DFS, Prim)</li>
 *   <li>Решение лабиринтов с использованием алгоритмов поиска пути (Dijkstra, A*)</li>
 *   <li>Сохранение и загрузка лабиринтов из файлов</li>
 *   <li>Визуализация лабиринтов в графическом окне</li>
 * </ul>
 * 
 * @author Academy Team
 * @version 1.0
 */
@Command(
        name = "maze-app",
        description = "Maze generator and solver CLI application.",
        version = "1.0",
        mixinStandardHelpOptions = true,
        subcommands = {Application.GenerateCommand.class, Application.SolveCommand.class})
public class Application implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final ObjectReader YAML_READER =
            new ObjectMapper(new YAMLFactory()).findAndRegisterModules().reader();

    /**
     * Команда для генерации лабиринта.
     * 
     * <p>Генерирует лабиринт заданного размера с использованием указанного алгоритма.
     * Поддерживаемые алгоритмы: dfs, prim.
     */
    @Command(name = "generate", description = "Generate a maze with specified algorithm and dimensions.")
    static class GenerateCommand implements Runnable {
        @Option(
                names = {"-w", "--width"},
                description = "Number of cells horizontally (maze width)",
                required = true)
        private int width;

        @Option(
                names = {"-h", "--height"},
                description = "Number of cells vertically (maze height)",
                required = true)
        private int height;

        @Option(
                names = {"-a", "--algorithm"},
                description = "Choose algorithm for build Maze",
                required = true)
        private String algorithm;

        @Option(
                names = {"-o", "--output"},
                description = "Output file to save Maze")
        private File output;

        @Option(names = "--window", description = "Show graphical window", defaultValue = "false")
        private boolean showWindow;

        public void run() {
            validateDimensions(width, height);
            Generator gen = ChooseGenerator.choose(algorithm);
            if (gen == null) {
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
            }
            Maze res = gen.generate(width, height);

            showWindowIfRequested(showWindow, res, null);

            if (output != null) {
                MazeIO.saveToFile(res, output, null);
            }
        }
    }

    /**
     * Команда для решения лабиринта.
     * 
     * <p>Находит путь от начальной точки до конечной в загруженном лабиринте
     * с использованием указанного алгоритма поиска пути.
     * Поддерживаемые алгоритмы: dijkstra, astar.
     */
    @Command(name = "solve", description = "Solve a maze with specified algorithm and points.")
    static class SolveCommand implements Runnable {

        @Option(
                names = {"-a", "--algorithm"},
                description = "Choose algorithm for build Maze",
                required = true)
        private String algorithm;

        @Option(
                names = {"--start"},
                description = "Write start point for Maze",
                required = true)
        private String start;

        @Option(
                names = {"--end"},
                description = "Write end point for Maze",
                required = true)
        private String end;

        @Option(
                names = {"-f", "--file"},
                description = "Input file with Maze")
        private File file;

        @Option(
                names = {"-o", "--output"},
                description = "Output file to save Maze")
        private File output;

        @Option(names = "--window", description = "Show graphical window", defaultValue = "false")
        private boolean showWindow;

        public void run() {
            Point startPoint;
            Point endPoint;
            try {
                startPoint = parse(start);
                endPoint = parse(end);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }

            if (file == null) {
                System.out.println("Input file is not specified");
                return;
            }

            Maze maze = MazeIO.loadFromFile(file);

            if (!inBounds(startPoint, maze) || !inBounds(endPoint, maze)) {
                System.out.println("Start or end point is out of maze bounds");
                return;
            }

            if (!isPathCell(startPoint, maze) || !isPathCell(endPoint, maze)) {
                System.out.println("Start or end point is inside a wall");
                return;
            }

            Solver solver = ChooseSolver.choose(algorithm);
            if (solver == null) {
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
            }
            Path path = solver.solve(maze, startPoint, endPoint);
            if (path == null || path.points().length == 0) {
                System.out.println("Path was not found.");
                JOptionPane.showMessageDialog(null, "Path was not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            showWindowIfRequested(showWindow, maze, path);

            if (output != null) {
                saveSolvedMaze(maze, path, output.toPath());
            }
        }

        private Point parse(String s) {
            if (s == null) {
                throw new IllegalArgumentException("Invalid point format: null, expected format: x,y");
            }
            String[] parts = s.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid point format: " + s + ", expected format: x,y");
            }
            try {
                int px = Integer.parseInt(parts[0].trim());
                int py = Integer.parseInt(parts[1].trim());
                return new Point(px, py);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid point format: " + s + ", expected format: x,y", e);
            }
        }

        private boolean inBounds(Point p, Maze maze) {
            return p.x() >= 0 && p.x() < maze.x() && p.y() >= 0 && p.y() < maze.y();
        }

        private boolean isPathCell(Point p, Maze maze) {
            return maze.cells()[p.x()][p.y()] == CellType.PATH;
        }

        private void saveSolvedMaze(Maze maze, Path path, java.nio.file.Path outputPath) {
            try {
                java.nio.file.Path parent = outputPath.toAbsolutePath().getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                CellType[][] cells = maze.cells();
                char[][] picture = new char[maze.x()][maze.y()];
                for (int i = 0; i < maze.x(); i++) {
                    for (int j = 0; j < maze.y(); j++) {
                        picture[i][j] = cells[i][j] == CellType.WALL ? '#' : ' ';
                    }
                }

                if (path != null && path.points().length > 0) {
                    Point[] points = path.points();
                    for (int i = 0; i < points.length; i++) {
                        Point point = points[i];
                        char mark = '.';
                        if (i == 0) {
                            mark = 'O';
                        } else if (i == points.length - 1) {
                            mark = 'X';
                        }
                        picture[point.x()][point.y()] = mark;
                    }
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < picture.length; i++) {
                    sb.append(picture[i]);
                    sb.append(System.lineSeparator());
                }

                Files.writeString(outputPath, sb.toString());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    /** Размер шрифта для конфигурации. */
    int fontSize;

    /** Массив слов для конфигурации. */
    private String[] words;

    /** Путь к файлу конфигурации. */
    private File configPath;

    /**
     * Точка входа в приложение.
     * 
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        if (args.length > 0 && "academy.Application".equals(args[0])) {
            args = Arrays.copyOfRange(args, 1, args.length);
        }
        new CommandLine(new Application()).execute(args);
    }

    @Override
    public void run() {
        var config = loadConfig();
        LOGGER.atInfo().addKeyValue("config", config).log("Config content");
    }

    /**
     * Валидирует размеры лабиринта.
     * 
     * @param width ширина лабиринта
     * @param height высота лабиринта
     * @throws IllegalArgumentException если ширина или высота неположительны
     */
    private static void validateDimensions(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive integers.");
        }
    }

    /**
     * Отображает графическое окно с лабиринтом, если это запрошено.
     * 
     * @param showWindow флаг необходимости отображения окна
     * @param maze лабиринт для отображения
     * @param path путь в лабиринте (может быть null)
     */
    private static void showWindowIfRequested(boolean showWindow, Maze maze, Path path) {
        if (!showWindow) {
            return;
        }
        SwingUtilities.invokeLater(() -> new MazeWindow(maze, path));
    }

    /**
     * Загружает конфигурацию приложения из файла или использует значения по умолчанию.
     * 
     * @return конфигурация приложения
     * @throws UncheckedIOException если произошла ошибка при чтении файла
     */
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
