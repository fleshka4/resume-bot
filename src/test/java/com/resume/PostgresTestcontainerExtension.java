package com.resume;

import org.junit.jupiter.api.extension.Extension;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresTestcontainerExtension implements Extension {

    private static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.1")
                .withDatabaseName("resume-bot");
        postgreSQLContainer.start();
    }

    public static class PostgresTestcontainerContextInitializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            var properties = TestPropertyValues.of(
                    "CONTAINER.USERNAME=" + postgreSQLContainer.getUsername(),
                    "CONTAINER.PASSWORD=" + postgreSQLContainer.getPassword(),
                    "CONTAINER.URL=" + postgreSQLContainer.getJdbcUrl()
            );
            properties.applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
