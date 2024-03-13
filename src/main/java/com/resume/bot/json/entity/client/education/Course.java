package com.resume.bot.json.entity.client.education;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Course {
    @NonNull
    private String name;

    @NonNull
    private String organization;

    private String result;

    private int year;

    @JsonCreator
    public static Course createCourse(
            @JsonProperty("name") String name,
            @JsonProperty("organization") String organization,
            @JsonProperty("result") String result,
            @JsonProperty("year") Integer year) {
        return new Course(name, organization, result, year);
    }
}
