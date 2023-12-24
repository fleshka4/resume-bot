package com.resume.hh_wrapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
public class ApiClientImpl {

    // todo: add error handlers
    private final WebClient webClient;

    public <T> T get(String uri, Class<T> type) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(type)
                .block();
    }

    public <T> List<T> getList(String uri, Class<T> type) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(type)
                .toStream()
                .toList();
    }

    public <T> T post(String uri, T body, Class<T> type) {
        return webClient.post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(type)
                .block();
    }

    public <T> T put(String uri, T body, Class<T> type) {
        return webClient.put()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(type)
                .block();
    }

}
