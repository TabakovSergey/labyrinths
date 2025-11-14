package academy.maze.HelpAlgorithm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EdgeTest {

    @Test
    void compareToShouldSortByWeight() {
        Edge lighter = new Edge(0, 0, 1, 0, 1);
        Edge heavier = new Edge(0, 0, 0, 1, 5);

        assertThat(lighter.compareTo(heavier)).isLessThan(0);
        assertThat(heavier.compareTo(lighter)).isGreaterThan(0);
    }
}
