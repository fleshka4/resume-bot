package com.resume.hh_wrapper;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class HhConfig {
    @Value("${hh.client_id}")
    private String clientId;

    @Value("${hh.client_secret}")
    private String clientSecret;
}
