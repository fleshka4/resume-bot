package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.model.entity.Template;
import com.resume.bot.repository.TemplateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

        Optional<Template> savedTemplateOpt = repository.findById(template.getTemplateId());

        assertTrue(savedTemplateOpt.isPresent());
        assertTrue(template.getTemplateId() == savedTemplateOpt.get().getTemplateId());
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
