package academy.maze.HelpAlgorithm;

import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuilderPath {
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
