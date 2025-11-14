package academy.maze.IO;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class MazeIO {
    public static Maze loadFromFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) {
                throw new IllegalArgumentException("Maze file is empty: " + file.getAbsolutePath());
            }
            int rows = lines.size();
            int cols = lines.get(0).length();
            if (cols == 0) {
                throw new IllegalArgumentException("Maze file contains an empty row: " + file.getAbsolutePath());
            }

            CellType[][] cells = new CellType[rows][cols];

            for (int i = 0; i < rows; i++) {
                String line = lines.get(i);
                if (line.length() != cols) {
                    throw new IllegalArgumentException("Maze file must be rectangular. Line " + (i + 1) + " has length "
                            + line.length() + ", expected " + cols);
                }
                for (int j = 0; j < cols; j++) {
                    cells[i][j] = (line.charAt(j) == '#') ? CellType.WALL : CellType.PATH;
                }
            }
            return new Maze(cells, rows, cols);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveToFile(Maze maze, File file, Path path) {
        StringBuilder sb = new StringBuilder();
        CellType[][] cells = maze.cells();
        Point[] points = path != null ? path.points() : new Point[0];
        boolean[][] pathMask = new boolean[maze.x()][maze.y()];
        for (Point point : points) {
            if (point == null) {
                continue;
            }
            int px = point.x();
            int py = point.y();
            if (px >= 0 && px < maze.x() && py >= 0 && py < maze.y()) {
                pathMask[px][py] = true;
            }
        }

        for (int i = 0; i < maze.x(); i++) {
            for (int j = 0; j < maze.y(); j++) {
                if (cells[i][j] == CellType.WALL) {
                    sb.append('#');
                } else if (pathMask[i][j]) {
                    sb.append('.');
                } else sb.append(' ');
            }

            sb.append(System.lineSeparator());
        }
        try {
            Files.writeString(file.toPath(), sb.toString());

        } catch (IOException e) {
            throw new RuntimeException("Failed to save maze to " + file.getAbsolutePath());
        }
    }
}
