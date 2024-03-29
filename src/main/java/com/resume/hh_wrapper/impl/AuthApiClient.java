package com.resume.hh_wrapper.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RequiredArgsConstructor
@Slf4j
public class AuthApiClient {
    private final WebClient webClient;

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
            log.error(("Error response: status={" + ex.getStatusCode() + "}, body={" + ex.getResponseBodyAsString() + ")}"));
            throw ex; // Rethrow the exception or handle it according to your application's needs
        }
    }
}
