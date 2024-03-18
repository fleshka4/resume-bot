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

    @NonNull
    private String id;

    @NonNull
    private String name;

    @JsonProperty("accept_incomplete_resumes")
    private boolean acceptIncompleteResumes;

    @JsonProperty("is_default")
    private boolean isDefault;

    @JsonCreator
    public static Role createRole(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("accept_incomplete_resumes") boolean acceptIncompleteResumes,
            @JsonProperty("is_default") boolean isDefault) {
        return new Role(id, name, acceptIncompleteResumes, isDefault);
    }
}
