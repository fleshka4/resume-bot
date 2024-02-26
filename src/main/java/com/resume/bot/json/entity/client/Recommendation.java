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
public class Recommendation {
    @NonNull
    private String contact;

    @NonNull
    private String name;

    @NonNull
    private String organization;

    @NonNull
    private String position;

    @JsonCreator
    public static Recommendation createRecommendation(
            @JsonProperty("contact") @NonNull String contact,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("organization") @NonNull String organization,
            @JsonProperty("position") @NonNull String position) {
        return new Recommendation(contact, name, organization, position);
    }
}
