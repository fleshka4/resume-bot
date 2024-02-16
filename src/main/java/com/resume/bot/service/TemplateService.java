package com.resume.bot.service;

import com.resume.bot.model.entity.Template;
import com.resume.bot.repository.TemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository repository;

    public Template saveTemplate(Template template) {
        return repository.save(template);
    }

    public Template getTemplate(int templateId) {
        return repository.findById(templateId).orElseThrow(
                () -> new EntityNotFoundException("Template not found")
        );
    }

    public List<Template> getTemplates() {
        return repository.findAll();
    }
}
