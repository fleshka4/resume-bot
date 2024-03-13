package com.resume.bot.json.entity.roles;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static Role createRole(
            @JsonProperty("accept_incomplete_resumes") boolean acceptIncompleteResumes,
            @JsonProperty("id") String id,
            @JsonProperty("is_default") boolean isDefault,
            @JsonProperty("name") String name) {
        return new Role(acceptIncompleteResumes, id, isDefault, name);
    }
}
