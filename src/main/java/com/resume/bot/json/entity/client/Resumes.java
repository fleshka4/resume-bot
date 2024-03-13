package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Resumes {
    private int found;

    private int page;

    private int pages;

    @JsonProperty("per_page")
    private int perPage;

    private List<Resume> items;

    @JsonCreator
    public static Resumes createResumes(
            @JsonProperty("found") int found,
            @JsonProperty("page") int page,
            @JsonProperty("pages") int pages,
            @JsonProperty("per_page") int perPage,
            @JsonProperty("items") List<Resume> items) {
        return new Resumes(found, page, pages, perPage, items);
    }
}
