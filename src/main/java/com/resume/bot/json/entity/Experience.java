package com.resume.bot.json.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @NonNull
    private String position;

    @NonNull
    private String start;
}
