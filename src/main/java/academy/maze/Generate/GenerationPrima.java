package academy.maze.Generate;

import academy.maze.HelpAlgorithm.Edge;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.PriorityQueue;
import java.util.Random;

public class GenerationPrima implements Generator {

    public Maze generate(int x, int y)
    {
        System.out.println("Generating Prima");
        //размеры либиринта
        int rows = 2 * x + 1;
        int cols = 2 * y + 1;

        CellType[][] cell = new CellType[rows][cols];
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cell[i][j] = CellType.WALL;
            }

        }
        boolean[][] visited = new boolean[x][y];
        PriorityQueue<Edge> edges = new PriorityQueue<>();

        int startX = 0, startY = 0;
        int startGridX = 2*startX + 1, startGridY = 2*startY + 1;
        cell[startGridX][startGridY] = CellType.PATH;
        visited[startX][startY] = true;

        Random random = new Random();
        addEdge(startX, startY, visited, edges, x, y, random);
        while(!edges.isEmpty())
        {
            Edge e = edges.poll();
            int x1 = e.GetX1(), y1 = e.GetY1();
            int x2 = e.GetX2(), y2 = e.GetY2();

            if(visited[x1][y1] && visited[x2][y2])
                continue;
            int fromX, fromY, toX, toY;
            if(visited[x1][y1] && !visited[x2][y2])
            {
                visited[x2][y2] = true;
                fromX = x1; fromY = y1;
                toX = x2; toY = y2;

            }
            else if(!visited[x1][y1] && visited[x2][y2])
            {
                visited[x1][y1] = true;
                fromX = x2; fromY = y2;
                toX = x1; toY = y1;
            }
            else
                continue;

            visited[toX][toY] = true;

            int fromGridX = 2*fromX + 1, fromGridY = 2*fromY + 1;
            int toGridX = 2*toX + 1, toGridY = 2*toY + 1;

            cell[toGridX][toGridY] = CellType.PATH;

            cell[(fromGridX + toGridX) / 2][(fromGridY + toGridY) / 2] = CellType.PATH;

            addEdge(toX, toY, visited, edges, x, y, random);
        }

        return new Maze(cell, rows, cols);
    }

    private void addEdge(int x, int y, boolean[][] visited, PriorityQueue<Edge> Queue, int rows, int cols, Random random)
    {
    if(x > 0 && !visited[x - 1][y])
        Queue.add(new Edge(x, y, x - 1, y, random.nextInt(10) + 1));
    if( x + 1 < rows && !visited[x + 1][y])
        Queue.add(new Edge(x, y, x + 1, y, random.nextInt(10) + 1));
    if( y > 0 && !visited[x][y - 1])
        Queue.add(new Edge(x, y, x, y - 1, random.nextInt(10) + 1));
    if( y + 1 < cols && !visited[x][y + 1])
        Queue.add(new Edge(x, y, x, y + 1, random.nextInt(10) + 1));
    }


}
