package com.resume.hh_wrapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ApiClientTokenImpl {
    private final WebClient webClient;

    public <T> T get(String uri, String bearerToken, Class<T> type) {
        WebClient webClientTokened = webClient.mutate()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .build();
        return webClientTokened
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(type)
                .block();
    }

    public <T> List<T> getList(String uri, String bearerToken, Class<T> type) {
        WebClient webClientTokened = webClient.mutate()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .build();
        return webClientTokened
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(type)
                .toStream()
                .toList();
    }

    public <T> T post(String uri, String bearerToken, T body, Class<T> type) {
        WebClient webClientTokened = webClient.mutate()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .build();
        return webClientTokened.post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(type)
                .block();
    }

    public <T> T put(String uri, String bearerToken, T body, Class<T> type) {
        WebClient webClientTokened = webClient.mutate()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .build();
        return webClientTokened.put()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(type)
                .block();
    }
}
