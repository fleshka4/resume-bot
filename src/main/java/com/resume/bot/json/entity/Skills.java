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
public class Skills {
    @NonNull
    private List<Type> items;

    @JsonCreator
    public static Skills createSkills(@JsonProperty("items") @NonNull List<Type> items) {
        return new Skills(items);
    }
}
