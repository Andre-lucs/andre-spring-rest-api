package org.andrelucs.examplerestapi.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.3");
    private static void startContainers(){
        Startables.deepStart(Stream.of(container)).join();
    }
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource(
                    "testcontainers",
                    Map.of(
                            "spring.datasource.url", container.getJdbcUrl(),
                            "spring.datasource.username", container.getUsername(),
                            "spring.datasource.password", container.getPassword()
                    )
            );
            environment.getPropertySources().addFirst(testContainers);
        }

    }

}
