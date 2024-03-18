package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Language {
    @NonNull
    private String id;

    @NonNull
    private String name;



    @NonNull
    private Type level;

    @JsonCreator
    public static Language createLanguage(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("level") @NonNull Type level) {
        return new Language(id, name, level);
    }
}
