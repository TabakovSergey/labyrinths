package academy.maze.View;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class MazePanel extends JPanel {
    private final Maze maze;
    private final Path path;

    public MazePanel(Maze maze, Path path) {
        this.maze = maze;
        this.path = path;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int rows = maze.x();
        int cols = maze.y();

        int cellSize = Math.max(1, Math.min(getWidth()/cols, getHeight()/rows));

        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < cols; c++)
            {
                CellType type = maze.cells()[r][c];

                if(type == CellType.WALL)
                {
                    g.setColor(Color.BLACK);
                }
                else{
                    g.setColor(Color.WHITE);
                }

                int x = c * cellSize;
                int y = r * cellSize;
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);

            }
        }
        if(path != null)
        {
            g.setColor(Color.GREEN);
            for(Point p : path.points())
            {
                int px = p.y() * cellSize;
                int py = p.x() * cellSize;
                g.fillOval(px, py, cellSize, cellSize);
            }
        }
    }
}
