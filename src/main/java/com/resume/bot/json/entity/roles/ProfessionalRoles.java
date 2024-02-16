package com.resume.bot.json.entity.roles;

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
}
