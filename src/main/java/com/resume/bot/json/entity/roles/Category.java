package com.resume.bot.json.entity.roles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private List<Role> roles;

    @JsonCreator
    public static Category createCategory(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("roles") @NonNull List<Role> roles) {
        return new Category(id, name, roles);
    }
}
