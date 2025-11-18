package academy.maze.Solve;

import academy.maze.HelpAlgorithm.Bounds;
import academy.maze.HelpAlgorithm.BuilderPath;
import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Решатель лабиринтов на основе алгоритма Дейкстры.
 * 
 * <p>Использует алгоритм Дейкстры для поиска кратчайшего пути в графе.
 * Алгоритм работает следующим образом:
 * <ol>
 *   <li>Инициализирует расстояние до всех вершин как бесконечность</li>
 *   <li>Устанавливает расстояние до стартовой точки как 0</li>
 *   <li>Использует приоритетную очередь для выбора вершины с минимальным расстоянием</li>
 *   <li>Обновляет расстояния до соседних вершин</li>
 *   <li>Продолжает до достижения конечной точки или исчерпания всех возможных путей</li>
 * </ol>
 * 
 * <p>Алгоритм гарантированно находит кратчайший путь, если он существует.
 * Все ребра имеют одинаковый вес (1), так как движение по лабиринту
 * происходит по единичным шагам.
 */
public class SolverDijkstra implements Solver {

    /**
     * {@inheritDoc}
     * 
     * <p>Находит кратчайший путь с использованием алгоритма Дейкстры.
     */
    @Override
    public Path solve(Maze maze, Point start, Point end) {
        int rows = maze.x();
        int cols = maze.y();
        CellType[][] grid = maze.cells();

        int[][] distance = new int[rows][cols];
        Point[][] parent = new Point[rows][cols];
        boolean[][] visited = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }
        PriorityQueue<PointDistance> pq = new PriorityQueue<>();
        distance[start.x()][start.y()] = 0;
        pq.add(new PointDistance(start, 0));

        int[][] dir = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!pq.isEmpty()) {
            PointDistance cur = pq.poll();
            Point p = cur.point();

            int x = p.x(), y = p.y();
            if (visited[x][y]) continue;
            visited[x][y] = true;

            if (x == end.x() && y == end.y()) return BuilderPath.buildPath(parent, start, end);

            for (int[] d : dir) {
                int newX = x + d[0], newY = y + d[1];
                if (!Bounds.inBounds(newX, newY, rows, cols)) continue;
                if (grid[newX][newY] == CellType.WALL) continue;

                int nd = distance[x][y] + 1;
                if (nd < distance[newX][newY]) {
                    distance[newX][newY] = nd;
                    parent[newX][newY] = p;
                    pq.add(new PointDistance(new Point(newX, newY), nd));
                }
            }
        }
        return new Path(new Point[0]);
    }

    /**
     * Вспомогательная запись для хранения точки и расстояния до неё.
     * Используется в приоритетной очереди алгоритма Дейкстры.
     * 
     * @param point точка в лабиринте
     * @param distance расстояние до точки от стартовой позиции
     */
    private record PointDistance(Point point, int distance) implements Comparable<PointDistance> {
        @Override
        public int compareTo(PointDistance o) {
            return Integer.compare(this.distance, o.distance);
        }
    }
}
