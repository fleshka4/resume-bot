package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.model.entity.Template;
import com.resume.bot.repository.TemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TemplateServiceTest extends IntegrationBaseTest {

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private TemplateService templateService;

    @Test
    public void testSaveTemplate() {
        Template template = Template.builder()
                .templateId(1)
                .build();

        templateService.saveTemplate(template);

        Template savedTemplate = repository.findById(template.getTemplateId()).get();

        assertEquals(template, savedTemplate);
    }

    @Test
    public void testGetTemplate() {
        Template template = Template.builder()
                .templateId(1)
                .build();

        repository.save(template);

        Template savedTemplate = templateService.getTemplate(template.getTemplateId());

        assertEquals(template, savedTemplate);
    }

    @Test
    public void testGetTemplateNotFound() {
        assertThrows(EntityNotFoundException.class, () -> templateService.getTemplate(1));
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testGetTemplates(int expectedSize, List<Template> templates) {
        repository.saveAll(templates);

        List<Template> retrievedTemplates = templateService.getTemplates();

        assertEquals(expectedSize, retrievedTemplates.size());
        assertEquals(templates, retrievedTemplates);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(0, List.of()),
                Arguments.of(1, Collections.singletonList(
                        Template.builder().templateId(1).build())),
                Arguments.of(3, Arrays.asList(
                        Template.builder().templateId(1).build(),
                        Template.builder().templateId(2).build(),
                        Template.builder().templateId(3).build()))
        );
    }
}
