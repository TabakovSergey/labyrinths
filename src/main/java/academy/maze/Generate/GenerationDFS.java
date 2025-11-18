package academy.maze.Generate;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Генератор лабиринтов на основе алгоритма поиска в глубину (DFS).
 * 
 * <p>Использует алгоритм обхода графа в глубину для создания лабиринта.
 * Алгоритм работает следующим образом:
 * <ol>
 *   <li>Начинает с случайной ячейки</li>
 *   <li>Выбирает случайного непосещенного соседа</li>
 *   <li>Удаляет стену между текущей ячейкой и выбранным соседом</li>
 *   <li>Продолжает рекурсивно для новой ячейки</li>
 *   <li>При отсутствии непосещенных соседей возвращается назад</li>
 * </ol>
 * 
 * <p>Результирующий лабиринт гарантированно имеет один уникальный путь
 * между любыми двумя ячейками.
 */
public class GenerationDFS implements Generator {
    /**
     * {@inheritDoc}
     * 
     * <p>Генерирует лабиринт с использованием алгоритма DFS.
     */
    @Override
    public Maze generate(int x, int y) {
        int rows = x * 2 + 1, cols = y * 2 + 1;
        CellType[][] cell = new CellType[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cell[i][j] = CellType.WALL;
            }
        }

        boolean[][] visited = new boolean[x][y];
        Deque<int[]> stack = new ArrayDeque<>();

        int startX = 0, startY = 0;
        visited[startX][startY] = true;
        cell[startX * 2 + 1][startY * 2 + 1] = CellType.PATH;
        stack.push(new int[] {startX, startY});
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int cx = current[0], cy = current[1];

            List<int[]> neighbors = getUnvisitedNeighbors(cx, cy, visited, x, y);

            if (!neighbors.isEmpty()) {
                int[] next = neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
                int nx = next[0], ny = next[1];

                visited[nx][ny] = true;

                removeWall(cx, cy, nx, ny, cell);

                stack.push(next);
            } else stack.pop();
        }

        return new Maze(cell, rows, cols);
    }

    /**
     * Получает список непосещенных соседних ячеек.
     * 
     * @param x координата X текущей ячейки
     * @param y координата Y текущей ячейки
     * @param visited массив посещенных ячеек
     * @param maxX максимальная координата X
     * @param maxY максимальная координата Y
     * @return список координат непосещенных соседей
     */
    private List<int[]> getUnvisitedNeighbors(int x, int y, boolean[][] visited, int maxX, int maxY) {
        List<int[]> neighbors = new ArrayList<>();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newX = x + dir[0], newY = y + dir[1];
            if (newX >= 0 && newX < maxX && newY >= 0 && newY < maxY && !visited[newX][newY]) {
                neighbors.add(new int[] {newX, newY});
            }
        }
        return neighbors;
    }

    /**
     * Удаляет стену между двумя соседними ячейками.
     * 
     * @param x1 координата X первой ячейки
     * @param y1 координата Y первой ячейки
     * @param x2 координата X второй ячейки
     * @param y2 координата Y второй ячейки
     * @param cell массив ячеек лабиринта
     */
    private void removeWall(int x1, int y1, int x2, int y2, CellType[][] cell) {
        int gridX1 = x1 * 2 + 1, gridY1 = y1 * 2 + 1, gridX2 = x2 * 2 + 1, gridY2 = y2 * 2 + 1;

        int wallX = gridX1 + ((gridX2 - gridX1) / 2);
        int wallY = gridY1 + ((gridY2 - gridY1) / 2);
        cell[wallX][wallY] = CellType.PATH;

        cell[gridX2][gridY2] = CellType.PATH;
        cell[gridX1][gridY1] = CellType.PATH;
    }
}
