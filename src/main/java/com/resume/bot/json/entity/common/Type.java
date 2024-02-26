package com.resume.bot.json.entity.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Type {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @JsonCreator
    public static Type createType(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name) {
        return new Type(id, name);
    }
}
