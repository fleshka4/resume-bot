package com.resume.bot.service;

import com.resume.bot.repository.ResumeRepository;

public class ResumeService {
    private LatexService latexService;
    private final ResumeRepository repository;

    public ResumeService(ResumeRepository repository) {
        this.repository = repository;
    }
}
