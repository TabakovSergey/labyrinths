package academy.maze.Solve;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;

/**
 * Интерфейс решателя лабиринтов.
 * 
 * <p>Определяет контракт для различных алгоритмов поиска пути в лабиринте.
 * Каждый решатель должен найти путь от начальной точки до конечной,
 * если такой путь существует.
 */
public interface Solver {

    /**
     * Находит путь от начальной точки до конечной в лабиринте.
     * 
     * <p>Если путь не найден, возвращается путь с пустым массивом точек.
     * 
     * @param maze лабиринт для решения
     * @param start начальная точка пути
     * @param end конечная точка пути
     * @return путь в лабиринте (массив точек от start до end),
     *         или путь с пустым массивом, если путь не найден
     */
    Path solve(Maze maze, Point start, Point end);
}
