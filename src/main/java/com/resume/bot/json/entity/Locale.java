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
    private String id;

    @NonNull
    private String name;

    @NonNull
    private Boolean current;

    @JsonCreator
    public static Locale createLocale(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("current") @NonNull Boolean current) {
        return new Locale(id, name, current);
    }
}
