package com.resume.bot.json.entity.client;

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
}
