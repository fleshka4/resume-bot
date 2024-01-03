package com.resume.hh_wrapper.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ApiClientTokenImpl {
    private final WebClient webClient;

    public <T> T get(String uri, String bearerToken, Class<T> type) {
        try {
            WebClient webClientTokened = webClient.mutate()
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .build();
            return webClientTokened
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(type)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error(("Error response: status={" + ex.getStatusCode() + "}, body={" + ex.getResponseBodyAsString() + ")}"));
            throw ex;
        }
    }

    public <T> List<T> getList(String uri, String bearerToken, Class<T> type) {
        try {
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
        } catch (WebClientResponseException ex) {
            log.error(("Error response: status={" + ex.getStatusCode() + "}, body={" + ex.getResponseBodyAsString() + ")}"));
            throw ex;
        }
    }

    public <T> ResponseEntity<T> post(String uri, String bearerToken, T body, Class<T> type) {
        try {
            WebClient webClientTokened = webClient.mutate()
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .build();
            return webClientTokened.post()
                    .uri(uri)
                    .bodyValue(body)
                    .retrieve()
                    .toEntity(type)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error(("Error response: status={" + ex.getStatusCode() + "}, body={" + ex.getResponseBodyAsString() + ")}"));
            throw ex;
        }
    }

    public <T> T put(String uri, String bearerToken, T body, Class<T> type) {
        try {
            WebClient webClientTokened = webClient.mutate()
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .build();
            return webClientTokened.put()
                    .uri(uri)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(type)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error(("Error response: status={" + ex.getStatusCode() + "}, body={" + ex.getResponseBodyAsString() + ")}"));
            throw ex;
        }
    }
}
