package com.resume.bot.service;

import com.resume.bot.model.entity.Resume;
import com.resume.bot.model.entity.Template;
import com.resume.bot.model.entity.User;
import com.resume.bot.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Resume getResumeByTitle(String title) {
        return repository.findByTitle(title).get();
    }

    public List<Resume> getResumesByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Resume> getResumesByUserId(Long id) {
        return repository.findByUser_TgUid(id);
    }

    public List<Resume> getHhResumesByUserId(Long id) {
        return repository.findByUser_TgUid(id).stream()
                .filter(resume -> resume.getLink() != null)
                .toList();
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

    public void updatePdfPathByResumeId(String pdfPath, int resumeId) {
        repository.updatePdfPathByResumeId(pdfPath, resumeId);
    }

    public void deleteResume(Resume resume) {
        repository.delete(resume);
    }
}
