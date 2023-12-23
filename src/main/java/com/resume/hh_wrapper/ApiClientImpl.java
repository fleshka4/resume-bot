package com.resume.hh_wrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

    public String auth(String uri, String body) {
        WebClient client = webClient.mutate()
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            return client.post()
                    .uri(uri)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException ex) {
            // Log details of the error response for debugging
            System.err.println(("Error response: status={" + ex.getStatusCode() + "}, body={" + ex.getResponseBodyAsString() + ")}"));
            throw ex; // Rethrow the exception or handle it according to your application's needs
        }
    }
}
