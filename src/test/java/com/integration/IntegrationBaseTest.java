package com.integration;

import com.resume.ResumeBotApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ResumeBotApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("tests")
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
