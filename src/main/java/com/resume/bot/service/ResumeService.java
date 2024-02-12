package com.resume.bot.service;

import com.resume.bot.model.entity.Resume;
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

    public List<Resume> getHhResumesByUserId(Long id) {
        return repository.findByUser_TgUid(id).stream()
                .filter(resume -> resume.getLink() != null)
                .toList();
    }

    public void updateHhLinkByResumeId(String hhLink, int resumeId) {
        repository.updateHhLinkByResumeId(hhLink, resumeId);
    }

    public void deleteResume(Resume resume) {
        repository.delete(resume);
    }
}
