package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.model.entity.Resume;
import com.resume.bot.model.entity.Template;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ResumeServiceTest extends IntegrationBaseTest {

    @Autowired
    private ResumeRepository repository;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserService userService;

    @Autowired
    private TemplateService templateService;

    @Test
    public void testGetResumeNotFound() {
        assertThrows(EntityNotFoundException.class, () -> resumeService.getResume(1));
    }

    @Test
    public void testSaveResume() {
        Resume resume = new Resume();
        resume.setResumeId(1);
        resume.setResumeData("aaa");
        resume.setTitle("Title");

        Resume savedResume = resumeService.saveResume(resume);

        assertNotNull(savedResume);
        assertEquals(resume.getResumeId(), savedResume.getResumeId());
        assertEquals(resume.getResumeData(), savedResume.getResumeData());
        assertEquals(resume.getTitle(), savedResume.getTitle());
    }

    @Test
    public void testGetResumeExists() {
        Resume resume = new Resume();
        resume.setResumeId(1);
        resume.setResumeData("aaa");
        resume.setTitle("Title");

        repository.save(resume);
        Resume retrievedResume = resumeService.getResume(1);

        assertNotNull(retrievedResume);
        assertEquals(resume.getTitle(), retrievedResume.getTitle());
    }

    @Test
    public void testGetResumeByTitle() {
        Resume resume = new Resume();
        resume.setTitle("Title");
        resume.setResumeId(1);
        resume.setResumeData("aaa");

        resumeService.saveResume(resume);
        Resume retrievedResume = resumeService.getResumeByTitle("Title");

        assertNotNull(retrievedResume);
        assertEquals(resume.getTitle(), retrievedResume.getTitle());
    }

    @Test
    public void testGetResumeByTitleAndUserTgUid() {
        long tgUid = 243433443L;

        User user = new User();
        user.setTgUid(tgUid);

        userService.saveUser(user);

        Resume resume = new Resume();
        resume.setTitle("Title");
        resume.setResumeId(1);
        resume.setResumeData("aaa");
        resume.setUser(user);

        resumeService.saveResume(resume);
        Optional<Resume> retrievedResume = resumeService.getResumeByTitle("Title", tgUid);

        assertTrue(retrievedResume.isPresent());
        assertEquals(resume.getTitle(), retrievedResume.get().getTitle());
        assertEquals(resume.getUser().getTgUid(), retrievedResume.get().getUser().getTgUid());
    }


    @Test
    public void testGetHhResumesByUserId() {
        Resume resume = new Resume();
        resume.setTitle("Title");
        resume.setResumeId(1);
        resume.setResumeData("aaa");

        Template template1 = new Template(1, "abc.img", "abc.pdf");
        resume.setTemplate(template1);

        templateService.saveTemplate(template1);

        resumeService.saveResume(resume);

        Resume found = resumeService.getResumeByTitle("Title");
        assertEquals(template1.getTemplateId(), found.getTemplate().getTemplateId());

        Template template2 = new Template(2, "bbc.img", "ggd.pdf");
        resume.setTemplate(template2);

        templateService.saveTemplate(template2);

        resumeService.saveResume(resume);

        found = resumeService.getResumeByTitle("Title");
        assertEquals(template2.getTemplateId(), found.getTemplate().getTemplateId());
    }
}
