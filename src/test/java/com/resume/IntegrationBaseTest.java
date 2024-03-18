package com.resume;

import com.resume.bot.repository.ResumeRepository;
import com.resume.bot.repository.TemplateRepository;
import com.resume.bot.repository.TokenHolderRepository;
import com.resume.bot.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    protected ResumeRepository resumeRepository;

    @Autowired
    protected TemplateRepository templateRepository;

    @Autowired
    protected TokenHolderRepository tokenHolderRepository;

    @Autowired
    protected UserRepository userRepository;

    protected static ConfigurableApplicationContext context;

    @AfterAll
    public static void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        tokenHolderRepository.deleteAll();
        resumeRepository.deleteAll();
        templateRepository.deleteAll();
    }

}
