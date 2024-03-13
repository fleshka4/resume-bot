package com.resume;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ExtendWith(PostgresTestcontainerExtension.class)
@ContextConfiguration(initializers = PostgresTestcontainerExtension.PostgresTestcontainerContextInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.config.location=classpath:application.properties"})
public abstract class IntegrationBaseTest {

    protected static ConfigurableApplicationContext context;

    @BeforeAll
    public static void setUp() {
//        context = SpringApplication.run(ResumeBotApplication.class);

//        createSchema();
    }

    @AfterAll
    public static void tearDown() {
        if (context != null) {
            context.close();
        }
    }

//    private static void createSchema() {
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test-resume-bot");
//        entityManagerFactory.close();
//    }
}
