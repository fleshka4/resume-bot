package com.resume.bot.service;

import com.resume.IntegrationBaseTest;
import com.resume.bot.model.entity.Resume;
import com.resume.bot.model.entity.Template;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.ResumeRepository;
import com.resume.bot.service.ResumeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResumeServiceTest extends IntegrationBaseTest {

    @Autowired
    private ResumeRepository repository;

    @Autowired
    private ResumeService resumeService;

    @Test
    public void testSaveResume() {
        Resume resume = new Resume();
        resume.setResumeId(1);
        resume.setResumeData("aaa");
        resume.setTitle("Title");

        Resume savedResume = resumeService.saveResume(resume);

        assertNotNull(savedResume);
        assertEquals(resume, savedResume);
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
        assertEquals(resume, retrievedResume);
    }

    @Test
    public void testGetResumeNotFound() {
        assertThrows(EntityNotFoundException.class, () -> resumeService.getResume(1));
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
        assertEquals(resume, retrievedResume);
    }

    @Test
    public void testGetResumeByTitleAndUserTgUid() {
        long tgUid = 243433443L;

        User user = new User();
        user.setTgUid(tgUid);

        Resume resume = new Resume();
        resume.setTitle("Title");
        resume.setResumeId(1);
        resume.setResumeData("aaa");
        resume.setUser(user);

        resumeService.saveResume(resume);
        Optional<Resume> retrievedResume = resumeService.getResumeByTitle("Title", tgUid);

        assertTrue(retrievedResume.isPresent());
        assertEquals(resume, retrievedResume.get());
    }


    @Test
    public void testGetHhResumesByUserId() {
        Resume resume = new Resume();
        resume.setTitle("Title");
        resume.setResumeId(1);
        resume.setResumeData("aaa");

        Template template1 = new Template(1, "abc.img", "abc.pdf");
        resume.setTemplate(template1);

        resumeService.saveResume(resume);

        Resume found = resumeService.getResumeByTitle("Title");
        assertEquals(template1, found.getTemplate());

        Template template2 = new Template(2, "bbc.img", "ggd.pdf");
        resume.setTemplate(template2);

        resumeService.saveResume(resume);

        found = resumeService.getResumeByTitle("Title");
        assertEquals(template2, found.getTemplate());
    }
}
