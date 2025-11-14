package academy.maze.Generate;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class GenerationDFS implements Generator {
    public Maze generate(int x, int y) {
        System.out.println("Generating DFS");
        int rows = x * 2 + 1, cols = y * 2 + 1;
        CellType[][] cell = new CellType[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cell[i][j] = CellType.WALL;
            }
        }

        boolean[][] visited = new boolean[x][y];
        Deque<int[]> stack = new ArrayDeque<>();
        Random random = new Random();

        int startX = 0, startY = 0;
        visited[startX][startY] = true;
        cell[startX * 2 + 1][startY * 2 + 1] = CellType.PATH;
        stack.push(new int[] {startX, startY});
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int cx = current[0], cy = current[1];

            List<int[]> neighbors = getUnvisitedNeighbors(cx, cy, visited, x, y);

            if (!neighbors.isEmpty()) {
                int[] next = neighbors.get(random.nextInt(neighbors.size()));
                int nx = next[0], ny = next[1];

                visited[nx][ny] = true;

                removeWall(cx, cy, nx, ny, cell);

                stack.push(next);
            } else stack.pop();
        }

        return new Maze(cell, rows, cols);
    }

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

    private void removeWall(int x1, int y1, int x2, int y2, CellType[][] cell) {
        int gridX1 = x1 * 2 + 1, gridY1 = y1 * 2 + 1, gridX2 = x2 * 2 + 1, gridY2 = y2 * 2 + 1;

        int wallX = (gridX1 + gridX2) / 2, wallY = (gridY1 + gridY2) / 2;
        cell[wallX][wallY] = CellType.PATH;

        cell[gridX2][gridY2] = CellType.PATH;
        cell[gridX1][gridY1] = CellType.PATH;
    }
}
