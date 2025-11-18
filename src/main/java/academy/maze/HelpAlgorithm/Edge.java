package academy.maze.HelpAlgorithm;

import org.jetbrains.annotations.NotNull;

/**
 * Ребро графа для алгоритма Прима.
 * 
 * <p>Представляет ребро между двумя ячейками лабиринта с весом.
 * Используется в алгоритме Прима для построения минимального остовного дерева.
 * Ребра сравниваются сначала по весу, затем по координатам вершин.
 */
public class Edge implements Comparable<Edge> {
    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;
    private final int weight;

    /**
     * Создает новое ребро между двумя ячейками.
     * 
     * @param x1 координата X первой ячейки
     * @param y1 координата Y первой ячейки
     * @param x2 координата X второй ячейки
     * @param y2 координата Y второй ячейки
     * @param weight вес ребра (используется для приоритизации в алгоритме Прима)
     */
    public Edge(int x1, int y1, int x2, int y2, int weight) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.weight = weight;
    }

    /**
     * Возвращает вес ребра.
     * 
     * @return вес ребра
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Возвращает координату X первой вершины.
     * 
     * @return координата X первой вершины
     */
    public int GetX1() {
        return x1;
    }

    /**
     * Возвращает координату X второй вершины.
     * 
     * @return координата X второй вершины
     */
    public int GetX2() {
        return x2;
    }

    /**
     * Возвращает координату Y первой вершины.
     * 
     * @return координата Y первой вершины
     */
    public int GetY1() {
        return y1;
    }

    /**
     * Возвращает координату Y второй вершины.
     * 
     * @return координата Y второй вершины
     */
    public int GetY2() {
        return y2;
    }

    @Override
    public int compareTo(@NotNull Edge other) {
        int result = Integer.compare(this.weight, other.weight);
        if (result != 0) return result;
        result = Integer.compare(this.x1, other.x1);
        if (result != 0) return result;
        result = Integer.compare(this.y1, other.y1);
        if (result != 0) return result;
        result = Integer.compare(this.x2, other.x2);
        if (result != 0) return result;
        return Integer.compare(this.y2, other.y2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge other)) return false;
        return x1 == other.x1 && x2 == other.x2 && y1 == other.y1 && y2 == other.y2 && weight == other.weight;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(x1);
        result = 31 * result + Integer.hashCode(x2);
        result = 31 * result + Integer.hashCode(y1);
        result = 31 * result + Integer.hashCode(y2);
        result = 31 * result + Integer.hashCode(weight);
        return result;
    }
}
