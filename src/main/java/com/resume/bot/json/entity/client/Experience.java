package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Experience {
    private Area area;

    private String company;

    @JsonProperty("company_id")
    private String companyId;

    @JsonProperty("company_url")
    private String companyUrl;

    private String description;

    private Employer employer;

    private String end;

    @NonNull
    private List<Type> industries;

    private Type industry;

    private String position;

    @NonNull
    private String start;

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
