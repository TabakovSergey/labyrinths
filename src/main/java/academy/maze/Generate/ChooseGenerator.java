package academy.maze.Generate;

/**
 * Фабрика для выбора генератора лабиринтов по имени алгоритма.
 * 
 * <p>Поддерживаемые алгоритмы:
 * <ul>
 *   <li>"dfs" - алгоритм поиска в глубину</li>
 *   <li>"prim" - алгоритм Прима</li>
 * </ul>
 */
public class ChooseGenerator {

    /**
     * Выбирает генератор лабиринта по имени алгоритма.
     * 
     * @param word имя алгоритма (регистр не важен)
     * @return экземпляр генератора или null, если алгоритм не найден
     */
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
