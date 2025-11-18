package academy.maze.View;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import javax.swing.JFrame;

/**
 * Графическое окно для отображения лабиринта.
 * 
 * <p>Создает Swing-окно с панелью для визуализации лабиринта и пути в нём.
 * Окно имеет фиксированный размер 800x800 пикселей и центрируется на экране.
 */
public class MazeWindow extends JFrame {
    /**
     * Создает новое окно для отображения лабиринта.
     * 
     * @param maze лабиринт для отображения
     * @param path путь в лабиринте (может быть null)
     */
    public MazeWindow(Maze maze, Path path) {
        setTitle("Maze");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        add(new MazePanel(maze, path));

        setVisible(true);
    }
}
