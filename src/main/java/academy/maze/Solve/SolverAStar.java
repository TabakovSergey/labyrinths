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
 * Решатель лабиринтов на основе алгоритма A* (A-star).
 * 
 * <p>Использует алгоритм A* для поиска оптимального пути с эвристикой.
 * Алгоритм работает следующим образом:
 * <ol>
 *   <li>Использует функцию оценки f(n) = g(n) + h(n), где:
 *       <ul>
 *         <li>g(n) - реальное расстояние от старта до текущей точки</li>
 *         <li>h(n) - эвристическая оценка расстояния от текущей точки до цели (манхэттенское расстояние)</li>
 *       </ul>
 *   </li>
 *   <li>Выбирает вершину с минимальным значением f(n) из приоритетной очереди</li>
 *   <li>Обновляет расстояния до соседних вершин</li>
 *   <li>Продолжает до достижения конечной точки</li>
 * </ol>
 * 
 * <p>Алгоритм A* обычно быстрее Дейкстры, так как использует эвристику
 * для направления поиска в сторону цели. При допустимой эвристике
 * (не переоценивающей расстояние) гарантированно находит оптимальный путь.
 */
public class SolverAStar implements Solver {
    /**
     * {@inheritDoc}
     * 
     * <p>Находит оптимальный путь с использованием алгоритма A*.
     */
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

    /**
     * Вычисляет эвристическую оценку расстояния между двумя точками.
     * 
     * <p>Используется манхэттенское расстояние (L1 норма),
     * которое является допустимой эвристикой для сетки.
     * 
     * @param a первая точка
     * @param b вторая точка
     * @return манхэттенское расстояние между точками
     */
    private int euristic(Point a, Point b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    /**
     * Узел для алгоритма A*.
     * 
     * <p>Содержит точку в лабиринте и значение функции оценки f(n).
     * Используется в приоритетной очереди для выбора следующей точки для обработки.
     */
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
