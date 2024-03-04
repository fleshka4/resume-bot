package com.resume.bot.json.entity.client;

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
public class Experience {
    private Area area;

    private String company;

    @JsonProperty("company_id")
    private String companyId;

    @JsonProperty("company_url")
    private String companyUrl;

    private String description;

    private Employer employer;

    private String end;

    @NonNull
    private List<Type> industries;

    private Type industry;

    private String position;

    @NonNull
    private String start;

    @JsonCreator
    public static Experience createExperience(
            @JsonProperty("area") Area area,
            @JsonProperty("company") String company,
            @JsonProperty("company_id") String companyId,
            @JsonProperty("company_url") String companyUrl,
            @JsonProperty("description") String description,
            @JsonProperty("employer") Employer employer,
            @JsonProperty("end") String end,
            @JsonProperty("industries") @NonNull List<Type> industries,
            @JsonProperty("industry") Type industry,
            @JsonProperty("position") String position,
            @JsonProperty("start") @NonNull String start) {
        return new Experience(area, company, companyId, companyUrl, description, employer, end,
                industries, industry, position, start);
    }
}
