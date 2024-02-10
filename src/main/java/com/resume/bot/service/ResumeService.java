package com.resume.bot.service;

import com.resume.bot.model.entity.Resume;
import com.resume.bot.model.entity.Template;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository repository;

    public Resume saveResume(Resume resume) {
        return repository.save(resume);
    }

    public Resume getResume(int resumeId) {
        return repository.findById(resumeId).orElseThrow(
                () -> new EntityNotFoundException("Resume not found")
        );
    }

    public List<Resume> getResumesByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Resume> getResumesByUserId(Long id) {
        return repository.findByUser_TgUid(id);
    }

    public void updateHhLinkByResumeId(String hhLink, int resumeId) {
        repository.updateHhLinkByResumeId(hhLink, resumeId);
    }

    public void updateTemplateByResumeId(Template template, int resumeId) {
        repository.updateTemplateByResumeId(template, resumeId);
    }

    public void updateResumeDataByResumeId(String resumeData, int resumeId) {
        repository.updateResumeDataByResumeId(resumeData, resumeId);
    }

    public void deleteResume(Resume resume) {
        repository.delete(resume);
    }
}
