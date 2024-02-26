package com.resume.bot.json.entity.client.education;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class PrimaryEducation {
    @NonNull
    private String name;

    @JsonProperty("name_id")
    private String nameId;

    private String organization;

    @JsonProperty("organization_id")
    private String organizationId;

    private String result;

    @JsonProperty("result_id")
    private String resultId;

    private long year;

    @JsonCreator
    public static PrimaryEducation createPrimaryEducation(
            @JsonProperty("name") String name,
            @JsonProperty("name_id") String nameId,
            @JsonProperty("organization") String organization,
            @JsonProperty("organization_id") String organizationId,
            @JsonProperty("result") String result,
            @JsonProperty("result_id") String resultId,
            @JsonProperty("year") long year) {
        return new PrimaryEducation(name, nameId, organization, organizationId, result, resultId, year);
    }
}
