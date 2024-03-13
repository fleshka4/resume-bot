package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogoUrls {
    @JsonProperty("90")
    private String ninety;

    @JsonProperty("240")
    private String twoForty;

    private String original;

    @JsonCreator
    public static LogoUrls createLogoUrls(
            @JsonProperty("90") String ninety,
            @JsonProperty("240") String twoForty,
            @JsonProperty("original") String original) {
        return new LogoUrls(ninety, twoForty, original);
    }
}
