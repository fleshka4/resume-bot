package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employer {
    @NonNull
    @JsonProperty("alternate_url")
    private String alternateUrl;

    @NonNull
    private String id;

    @JsonProperty("logo_urls")
    private LogoUrls logoUrls;

    @NonNull
    private String name;

    @NonNull
    private String url;

    @JsonCreator
    public static Employer createEmployer(
            @JsonProperty("alternate_url") @NonNull String alternateUrl,
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("logo_urls") LogoUrls logoUrls,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("url") @NonNull String url) {
        return new Employer(alternateUrl, id, logoUrls, name, url);
    }
}
