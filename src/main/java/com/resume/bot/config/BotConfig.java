package com.resume.bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String token;

    @Bean(name = "botName")
    public String botName() {
        return botName;
    }

    @Bean(name = "token")
    public String token() {
        return token;
    }
}
