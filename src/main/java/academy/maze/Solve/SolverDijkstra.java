package academy.maze.Solve;

import academy.maze.HelpAlgorithm.Bounds;
import academy.maze.HelpAlgorithm.BuilderPath;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class SolverDijkstra implements Solver{

    @Override
    public Path solve(Maze maze, Point start, Point end) {
        int rows = maze.x();
        int cols = maze.y();
        CellType [][] grid = maze.cells();

        int [][] distance = new int[rows][cols];
        Point [][] parent = new Point[rows][cols];
        boolean[][] visited = new boolean[rows][cols];

        for(int i = 0; i < rows; i++)
        {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }
        PriorityQueue<PointDistance> pq = new PriorityQueue<>();
        distance[start.x()][start.y()] = 0;
        pq.add(new PointDistance(start, 0));

        int[][] dir = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        while (!pq.isEmpty()) {
            PointDistance cur = pq.poll();
            Point p = cur.point();

            int x = p.x(), y = p.y();
            if(visited[x][y] )continue;
            visited[x][y] = true;

            if(x == end.x() && y == end.y())
                return BuilderPath.buildPath(parent, start, end);

            for(int[] d : dir) {
                int newX = x + d[0], newY = y + d[1];
                if(!Bounds.inBounds(newX, newY, rows, cols)) continue;
                if(grid[newX][newY] == CellType.WALL) continue;

                int nd = distance[x][y] + 1;
                if(nd < distance[newX][newY]) {
                    distance[newX][newY] = nd;
                    parent[newX][newY] = p;
                    pq.add(new PointDistance(new Point(newX, newY), nd));
                }
            }
        }
        return new Path(new Point[0]);
    }

    private record PointDistance(Point point, int distance) implements Comparable<PointDistance> {
        @Override
        public int compareTo(PointDistance o) {
            return Integer.compare(this.distance, o.distance);
        }
    }
}
