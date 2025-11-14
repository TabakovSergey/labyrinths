package academy.maze.View;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;

public class MazePrinter {
    public static void printMazeNotPath(Maze maze){
        CellType[][] cells = maze.cells();
        for(int i = 0; i < maze.x(); i++)
        {
            for (int j = 0; j < maze.y(); j++)
            {
                if(cells[i][j] == CellType.WALL)
                    System.out.print("#");
                else
                    System.out.print(" ");
            }
            System.out.println();
        }

    }

    public static void printMazePath(Maze maze, Path path){
        CellType[][] cells = maze.cells();
        Point[] points = path.points();

        for(int i = 0; i < maze.x(); i++){
            for (int j = 0; j < maze.y(); j++){
                if(cells[i][j] == CellType.WALL) {
                    System.out.print("#");
                    continue;
                }

                boolean isPathPoint = false;
                for(Point p : points){
                    if(p.x() == i && p.y() == j)
                    {
                        isPathPoint = true;
                        break;
                    }
                }
                if(isPathPoint)
                    System.out.print(".");
                else
                    System.out.print(" ");

            }
            System.out.println();
        }
    }
}
