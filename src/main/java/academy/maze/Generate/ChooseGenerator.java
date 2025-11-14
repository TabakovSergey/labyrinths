package academy.maze.Generate;

public class ChooseGenerator {

    public static Generator choose(String word) {
        if (word == null) {
            return null;
        }
        return switch (word.toLowerCase()) {
            case "prim" -> new GenerationPrima();
            case "dfs" -> new GenerationDFS();
            default -> null;
        };
    }
}
