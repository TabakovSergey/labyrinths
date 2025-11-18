package academy.maze.HelpAlgorithm;

import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Утилитный класс для построения пути из матрицы предков.
 * 
 * <p>Используется алгоритмами поиска пути (Дейкстра, A*) для восстановления
 * найденного пути из матрицы, которая хранит для каждой ячейки её предка
 * в оптимальном пути.
 */
public class BuilderPath {
    /**
     * Строит путь от начальной точки до конечной, используя матрицу предков.
     * 
     * <p>Алгоритм:
     * <ol>
     *   <li>Начинает с конечной точки</li>
     *   <li>Рекурсивно переходит к предку каждой точки</li>
     *   <li>Собирает все точки в список</li>
     *   <li>Разворачивает список для получения пути от start до end</li>
     * </ol>
     * 
     * @param parent матрица предков [x][y] -> предок точки (x, y)
     * @param start начальная точка пути
     * @param end конечная точка пути
     * @return путь от start до end в виде массива точек
     */
    public static Path buildPath(Point[][] parent, Point start, Point end) {
        List<Point> path = new ArrayList<>();
        Point current = end;
        while (current != null) {
            path.add(current);
            if (current.equals(start)) break;
            current = parent[current.x()][current.y()];
        }
        Collections.reverse(path);
        return new Path(path.toArray(Point[]::new));
    }
}
