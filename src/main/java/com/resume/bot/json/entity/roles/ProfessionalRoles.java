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
public class ProfessionalRoles {
    @NonNull
    private List<Category> categories;

    @JsonCreator
    public static ProfessionalRoles createProfessionalRoles(
            @JsonProperty("categories") @NonNull List<Category> categories) {
        return new ProfessionalRoles(categories);
    }
}
