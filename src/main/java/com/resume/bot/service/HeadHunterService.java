package com.resume.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class HeadHunterService {
    private TokenHolderService tokenHolderService;
    private ResumeService resumeService;

    /*public Metro getMetro(int id) {
        return get("metro/" + id, Metro.class);
    }*/


}
