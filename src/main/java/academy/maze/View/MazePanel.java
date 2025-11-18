package academy.maze.View;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Панель для отрисовки лабиринта и пути.
 * 
 * <p>Отрисовывает лабиринт в виде сетки, где:
 * <ul>
 *   <li>Черные ячейки - стены</li>
 *   <li>Белые ячейки - проходимые пути</li>
 *   <li>Зеленые круги - точки найденного пути</li>
 * </ul>
 * 
 * <p>Размер ячеек автоматически вычисляется на основе размера панели
 * и размеров лабиринта для оптимального отображения.
 */
public class MazePanel extends JPanel {
    /** Лабиринт для отображения. */
    private final Maze maze;
    
    /** Путь в лабиринте (может быть null). */
    private final Path path;

    /**
     * Создает новую панель для отображения лабиринта.
     * 
     * @param maze лабиринт для отображения
     * @param path путь в лабиринте (может быть null)
     */
    public MazePanel(Maze maze, Path path) {
        this.maze = maze;
        this.path = path;
    }

    /**
     * Отрисовывает лабиринт и путь на панели.
     * 
     * @param g графический контекст для отрисовки
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int rows = maze.x();
        int cols = maze.y();

        int cellSize = Math.max(1, Math.min(getWidth() / cols, getHeight() / rows));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CellType type = maze.cells()[r][c];

                if (type == CellType.WALL) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }

                int x = c * cellSize;
                int y = r * cellSize;
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
        if (path != null) {
            g.setColor(Color.GREEN);
            for (Point p : path.points()) {
                int px = p.y() * cellSize;
                int py = p.x() * cellSize;
                g.fillOval(px, py, cellSize, cellSize);
            }
        }
    }
}
