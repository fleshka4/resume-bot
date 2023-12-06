package com.resume.bot.service;

import com.resume.metro.Metro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HeadHunterService {
    private TokenHolderService tokenHolderService;
    private ResumeService resumeService;

    /*public Metro getMetro(int id) {
        return get("metro/" + id, Metro.class);
    }*/


}
