package com.resume.bot.json.entity.client;

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
}
