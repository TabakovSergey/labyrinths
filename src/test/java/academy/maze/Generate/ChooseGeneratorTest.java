package academy.maze.Generate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ChooseGeneratorTest {

    @Test
    void chooseShouldReturnPrimGenerator() {
        assertThat(ChooseGenerator.choose("prim")).isInstanceOf(GenerationPrima.class);
    }

    @Test
    void chooseShouldReturnDfsGenerator() {
        assertThat(ChooseGenerator.choose("dfs")).isInstanceOf(GenerationDFS.class);
    }

    @Test
    void chooseShouldReturnNullForUnknownAlgorithm() {
        assertThat(ChooseGenerator.choose("unknown")).isNull();
    }
}
