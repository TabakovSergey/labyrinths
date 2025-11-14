package academy.maze.HelpAlgorithm;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Edge implements Comparable<Edge> {
    private int x1, x2;
    private int y1, y2;
    private int weight;
    private List<Edge> edges = new ArrayList<Edge>();
    private Random random = new Random();

    public Edge(int x1, int y1, int x2, int y2, int weight) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public int GetX1()
    {
        return x1;
    }
    public int GetX2()
    {
        return x2;
    }
    public int GetY1()
    {
        return y1;
    }
    public int GetY2()
    {
        return y2;
    }
    @Override
    public int compareTo(@NotNull Edge other) {
        return Integer.compare(this.getWeight(), other.getWeight());
    }
}
