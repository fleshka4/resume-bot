package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.model.entity.Template;
import com.resume.bot.repository.TemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testGetTemplates() {
        List<Template> templates = List.of(
                Template.builder()
                        .templateId(1)
                        .build(),
                Template.builder()
                        .templateId(2)
                        .build(),
                Template.builder()
                        .templateId(3)
                        .build()
        );

        repository.saveAll(templates);

        List<Template> retrievedTemplates = templateService.getTemplates();
        assertEquals(templates.size(), retrievedTemplates.size());
        assertTrue(retrievedTemplates.containsAll(templates));
    }
}
