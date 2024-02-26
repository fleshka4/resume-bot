package com.resume.bot.json.entity.client.education;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Education {
    private List<Course> additional;

    private List<Course> attestation;

    private List<ElementaryEducation> elementary;

    private Type level;

    private List<PrimaryEducation> primary;

    @JsonCreator
    public static Education createEducation(
            @JsonProperty("additional") List<Course> additional,
            @JsonProperty("attestation") List<Course> attestation,
            @JsonProperty("elementary") List<ElementaryEducation> elementary,
            @JsonProperty("level") Type level,
            @JsonProperty("primary") List<PrimaryEducation> primary) {
        return new Education(additional, attestation, elementary, level, primary);
    }
}
