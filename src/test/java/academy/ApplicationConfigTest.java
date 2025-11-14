package academy;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ApplicationConfigTest {

    @Test
    void loadConfigShouldUseCliValuesWhenNoFileProvided() throws Exception {
        Application app = new Application();
        setField(app, "fontSize", 24);
        setField(app, "words", new String[] {"foo", "bar"});

        AppConfig config = invokeLoadConfig(app);

        assertThat(config.fontSize()).isEqualTo(24);
        assertThat(config.words()).containsExactly("foo", "bar");
    }

    @Test
    void loadConfigShouldReadYamlFileWhenProvided() throws Exception {
        Application app = new Application();
        Path tmp = Files.createTempFile("config", ".yaml");
        Files.writeString(
                tmp,
                """
                fontSize: 12
                words:
                  - hello
                  - world
                """);
        setField(app, "configPath", tmp.toFile());

        AppConfig config = invokeLoadConfig(app);

        assertThat(config.fontSize()).isEqualTo(12);
        assertThat(config.words()).containsExactly("hello", "world");
    }

    private static void setField(Application app, String name, Object value) throws Exception {
        Field field = Application.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(app, value);
    }

    private static AppConfig invokeLoadConfig(Application app) throws Exception {
        Method method = Application.class.getDeclaredMethod("loadConfig");
        method.setAccessible(true);
        return (AppConfig) method.invoke(app);
    }
}
