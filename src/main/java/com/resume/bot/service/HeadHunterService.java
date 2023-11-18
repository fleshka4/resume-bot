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
    private final WebClient webClient;

    public Metro getMetro(int id) {
        return get("metro/" + id, Metro.class);
    }

    public <T> T get(String uri, Class<T> clazz) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(clazz)
                .doOnError(error -> {
                    System.out.println(error);
                })
                .block();
    }

    public List<Metro> getFlux() {
        return webClient
                .get()
                .uri("metro/1")
                .retrieve()
                .bodyToFlux(Metro.class)
                .toStream()
                .toList();
    }
}
