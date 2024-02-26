package com.resume.bot.json.entity.roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Data
public class ProfessionalRoles {
    @NonNull
    private List<Category> categories;
}
