package academy.maze.View;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import javax.swing.JFrame;

public class MazeWindow extends JFrame {
    public MazeWindow(Maze maze, Path path) {
        setTitle("Maze");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        add(new MazePanel(maze, path));

        setVisible(true);
    }
}
