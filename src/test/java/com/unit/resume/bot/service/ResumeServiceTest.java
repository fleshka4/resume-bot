package com.unit.resume.bot.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResumeServiceTest {

    @Mock
    private ResumeRepository repository;

    @InjectMocks
    private ResumeService resumeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveResume() {
        Resume resume = new Resume();

        when(repository.save(resume)).thenReturn(resume);

        Resume savedResume = resumeService.saveResume(resume);

        assertNotNull(savedResume);
        assertEquals(resume, savedResume);
        verify(repository, times(1)).save(resume);
    }

    @Test
    public void testGetResumeExists() {
        Resume resume = new Resume();

        when(repository.findById(anyInt())).thenReturn(Optional.of(resume));

        Resume retrievedResume = resumeService.getResume(1);

        assertNotNull(retrievedResume);
        assertEquals(resume, retrievedResume);
    }

    @Test
    public void testGetResumeNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> resumeService.getResume(1));
    }

    @Test
    public void testGetResumeByTitle() {
        Resume resume = new Resume();

        when(repository.findByTitle(anyString())).thenReturn(Optional.of(resume));

        Resume retrievedResume = resumeService.getResumeByTitle("Title");

        assertNotNull(retrievedResume);
        assertEquals(resume, retrievedResume);
    }

    @Test
    public void testGetResumeByTitleAndUserTgUid() {
        User user = new User();
        user.setTgUid(243433443L);

        Long userTgUid = user.getTgUid();

        Resume resume = new Resume();
        resume.setUser(user);

        when(repository.findAllByTitle(anyString())).thenReturn(List.of(resume));

        Optional<Resume> retrievedResume = resumeService.getResumeByTitle("Title", userTgUid);

        assertTrue(retrievedResume.isPresent());
        assertEquals(resume, retrievedResume.get());
    }

    @Test
    public void testUpdateHhLinkByResumeId() {
        String hhLink = "https://example.com";
        int resumeId = 1;

        resumeService.updateHhLinkByResumeId(hhLink, resumeId);
        verify(repository, times(1)).updateHhLinkByResumeId(hhLink, resumeId);
    }

    @Test
    public void testUpdateTemplateByResumeId() {
        Template template = new Template();
        int resumeId = 1;

        resumeService.updateTemplateByResumeId(template, resumeId);
        verify(repository, times(1)).updateTemplateByResumeId(template, resumeId);
    }

    @Test
    public void testUpdateResumeDataByResumeId() {
        String resumeData = "Updated resume data";
        int resumeId = 1;


        resumeService.updateResumeDataByResumeId(resumeData, resumeId);
        verify(repository, times(1)).updateResumeDataByResumeId(resumeData, resumeId);
    }

    @Test
    public void testUpdatePdfPathByResumeId() {
        String pdfPath = "updated/path/to/pdf";
        int resumeId = 1;

        resumeService.updatePdfPathByResumeId(pdfPath, resumeId);
        verify(repository, times(1)).updatePdfPathByResumeId(pdfPath, resumeId);
    }

    @Test
    public void testDeleteResume() {
        Resume resume = new Resume();

        resumeService.deleteResume(resume);
        verify(repository, times(1)).delete(resume);
    }

    @Test
    public void testGetResumesByUser() {
        User user = new User();

        List<Resume> resumes = new ArrayList<>();
        resumes.add(new Resume());
        resumes.add(new Resume());

        when(repository.findByUser(user)).thenReturn(resumes);

        List<Resume> retrievedResumes = resumeService.getResumesByUser(user);
        assertNotNull(retrievedResumes);
        assertEquals(resumes.size(), retrievedResumes.size());
        assertTrue(retrievedResumes.containsAll(resumes));
    }

    @Test
    public void testGetResumesByUserId() {
        Long userId = 1L;

        List<Resume> resumes = new ArrayList<>();
        resumes.add(new Resume());
        resumes.add(new Resume());

        when(repository.findByUser_TgUid(userId)).thenReturn(resumes);
        List<Resume> retrievedResumes = resumeService.getResumesByUserId(userId);

        assertNotNull(retrievedResumes);
        assertEquals(resumes.size(), retrievedResumes.size());
        assertTrue(retrievedResumes.containsAll(resumes));
    }

    @Test
    public void testGetHhResumesByUserId() {
        Long userIdOne = 1L;

        List<Resume> resumes = new ArrayList<>();

        User userOne = new User();
        userOne.setTgUid(userIdOne);

        Resume resumeOne = new Resume();
        resumeOne.setUser(userOne);
        resumeOne.setLink("https://example.com");

        resumes.add(resumeOne);

        when(repository.findByUser_TgUid(userIdOne)).thenReturn(resumes);
        List<Resume> retrievedResumes = resumeService.getHhResumesByUserId(userIdOne);

        assertNotNull(retrievedResumes);
        assertEquals(1, retrievedResumes.size());
        assertTrue(retrievedResumes.contains(resumes.getFirst()));
    }
}
