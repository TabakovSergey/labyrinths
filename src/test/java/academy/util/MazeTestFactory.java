package academy.util;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;

/**
 * Small helpers for turning ASCII mazes into {@link Maze} objects for tests.
 */
public final class MazeTestFactory {
    private MazeTestFactory() {}

    public static Maze fromStringGrid(String... rows) {
        if (rows.length == 0) {
            throw new IllegalArgumentException("Maze must contain at least one row");
        }
        int cols = rows[0].length();
        CellType[][] cells = new CellType[rows.length][cols];
        for (int i = 0; i < rows.length; i++) {
            if (rows[i].length() != cols) {
                throw new IllegalArgumentException("All rows must have the same length");
            }
            for (int j = 0; j < cols; j++) {
                cells[i][j] = rows[i].charAt(j) == '#' ? CellType.WALL : CellType.PATH;
            }
        }
        return new Maze(cells, rows.length, cols);
    }
}
