package com.resume.hh_wrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
public class ApiClientImpl implements ApiClient {

    // todo: add error handlers
    private final WebClient webClient;
    @Override
    public <T> T get(String uri, Class<T> type) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(type)
                .block();
    }

    @Override
    public <T> List<T> getList(String uri, Class<T> type) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(type)
                .toStream()
                .toList();
    }

    @Override
    public <T> T post(String uri, T body, Class<T> type) {
        return webClient.post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(type)
                .block();
    }
}
