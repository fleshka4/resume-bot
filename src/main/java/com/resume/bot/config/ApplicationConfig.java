package com.resume.bot.config;

import com.resume.hh_wrapper.impl.ApiClientImpl;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
import com.resume.hh_wrapper.impl.AuthApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {

    @Value("${hh.base-url}")
    private String hhBaseUrl;

    @Value("${json.max.size.mb}")
    private int limit;

    @Bean
    public WebClient webClient() {
        int limitInBytes = limit * 1024 * 1024;
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(limitInBytes))
                .build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(hhBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public ApiClientImpl apiClientImpl() {
        return new ApiClientImpl(webClient());
    }

    @Bean
    public AuthApiClient authApiClient() {
        return new AuthApiClient(webClient());
    }

    @Bean
    public ApiClientTokenImpl apiClientTokenImpl() {
        return new ApiClientTokenImpl(webClient());
    }
}
