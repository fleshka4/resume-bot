package com.resume.bot.json.entity.client.education;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class ElementaryEducation {
    @NonNull
    private String name;

    private long year;

    @JsonCreator
    public static ElementaryEducation createElementaryEducation(
            @JsonProperty("name") String name,
            @JsonProperty("year") long year) {
        return new ElementaryEducation(name, year);
    }
}
