package com.resume.bot.json.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Locale {
    @NonNull
    private Boolean current;

    @NonNull
    private String id;

    @NonNull
    private String name;

    @JsonCreator
    public static Locale createLocale(
            @JsonProperty("current") @NonNull Boolean current,
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name) {
        return new Locale(current, id, name);
    }
}
