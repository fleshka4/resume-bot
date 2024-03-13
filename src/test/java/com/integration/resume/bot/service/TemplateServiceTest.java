package com.integration.resume.bot.service;

import com.integration.IntegrationBaseTest;
import com.resume.bot.model.entity.Template;
import com.resume.bot.repository.TemplateRepository;
import com.resume.bot.service.TemplateService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TemplateServiceTest extends IntegrationBaseTest {

//    @Mock
    @Autowired
    private TemplateRepository repository;

    @Autowired
    private TemplateService templateService;

//    @BeforeEach
//    public void setUp() {
//    }

    @Test
    public void testSaveTemplate() {
        Template template = new Template();

        repository.save(template);

        Template savedTemplate = templateService.saveTemplate(template);

        assertNotNull(savedTemplate);
        assertEquals(template, savedTemplate);
    }

//    @Test
//    public void testGetTemplateExists() {
//        Template template = new Template();
//
//        when(repository.findById(anyInt())).thenReturn(Optional.of(template));
//
//        Template retrievedTemplate = templateService.getTemplate(1);
//
//        assertNotNull(retrievedTemplate);
//        assertEquals(template, retrievedTemplate);
//    }
//
//    @Test
//    public void testGetTemplateNotFound() {
//        when(repository.findById(anyInt())).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> templateService.getTemplate(1));
//    }
//
//    @Test
//    public void testGetTemplates() {
//        List<Template> templates = Arrays.asList(
//                new Template(),
//                new Template(),
//                new Template()
//        );
//
//        when(repository.findAll()).thenReturn(templates);
//
//        List<Template> retrievedTemplates = templateService.getTemplates();
//        assertNotNull(retrievedTemplates);
//        assertEquals(templates.size(), retrievedTemplates.size());
//        assertTrue(retrievedTemplates.containsAll(templates));
//    }
}
