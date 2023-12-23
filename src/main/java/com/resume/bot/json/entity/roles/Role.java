package com.resume.bot.json.entity.roles;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {
    @JsonProperty("accept_incomplete_resumes")
    private boolean acceptIncompleteResumes;

    @NonNull
    private String id;

    @JsonProperty("is_default")
    private boolean isDefault;

    @NonNull
    private String name;
}
