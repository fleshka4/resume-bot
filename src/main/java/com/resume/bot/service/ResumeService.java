package com.resume.bot.service;

import com.resume.bot.model.entity.Resume;
import com.resume.bot.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
