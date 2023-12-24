package com.resume.bot.json.entity.roles;

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
}
