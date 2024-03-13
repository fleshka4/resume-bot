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
public class Id {
    @NonNull
    private String id;

    @JsonCreator
    public static Id createId(@JsonProperty("id") @NonNull String id) {
        return new Id(id);
    }
}
