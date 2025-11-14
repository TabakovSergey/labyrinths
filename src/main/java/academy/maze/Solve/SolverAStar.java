package academy.maze.Solve;

import academy.maze.HelpAlgorithm.Bounds;
import academy.maze.HelpAlgorithm.BuilderPath;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.Arrays;
import java.util.PriorityQueue;

public class SolverAStar implements Solver {
    @Override
    public Path solve(Maze maze, Point start, Point end) {
        int rows = maze.x(), cols = maze.y();
        CellType[][] grid = maze.cells();

        int[][] gScore = new int[rows][cols];
        Point[][] parent = new Point[rows][cols];
        boolean[][] visited = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            Arrays.fill(gScore[i], Integer.MAX_VALUE);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        gScore[start.x()][start.y()] = 0;
        pq.add(new Node(start, euristic(start, end)));

        int[][] dir = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            Point p = current.point;

            if (visited[p.x()][p.y()]) continue;
            visited[p.x()][p.y()] = true;
            if (p.equals(end)) return BuilderPath.buildPath(parent, start, end);

            for (int[] d : dir) {
                int nx = p.x() + d[0], ny = p.y() + d[1];
                if (!Bounds.inBounds(nx, ny, rows, cols)) continue;
                if (grid[nx][ny] == CellType.WALL) continue;

                int tentative = gScore[p.x()][p.y()] + 1;
                if (tentative < gScore[nx][ny]) {
                    gScore[nx][ny] = tentative;
                    parent[nx][ny] = p;
                    int f = tentative + euristic(new Point(nx, ny), end);
                    pq.add(new Node(new Point(nx, ny), f));
                }
            }
        }
        return new Path(new Point[0]);
    }

    private int euristic(Point a, Point b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    private static class Node implements Comparable<Node> {
        private final Point point;
        private final int f;

        public Node(Point point, int f) {
            this.point = point;
            this.f = f;
        }

        @Override
        public int compareTo(Node o) {
            int cmp = Integer.compare(this.f, o.f);
            if (cmp != 0) {
                return cmp;
            }
            cmp = Integer.compare(this.point.x(), o.point.x());
            if (cmp != 0) {
                return cmp;
            }
            return Integer.compare(this.point.y(), o.point.y());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Node other)) return false;
            return this.f == other.f && this.point.equals(other.point);
        }

        @Override
        public int hashCode() {
            int result = Integer.hashCode(f);
            result = 31 * result + point.hashCode();
            return result;
        }
    }
}
