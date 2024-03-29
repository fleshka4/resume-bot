package com.resume.bot.json.entity;

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
public class Industry {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private List<Type> industries;

    @JsonCreator
    public static Industry createIndustry(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("industries") @NonNull List<Type> industries) {
        return new Industry(id, name, industries);
    }
}
