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
public class Portfolio {
    private String description;

    @NonNull
    private String medium;

    @NonNull
    private String small;

    @NonNull
    private String id;

    @JsonCreator
    public static Portfolio createPortfolio(
            @JsonProperty("description") String description,
            @JsonProperty("medium") @NonNull String medium,
            @JsonProperty("small") @NonNull String small,
            @JsonProperty("id") @NonNull String id) {
        return new Portfolio(description, medium, small, id);
    }
}
