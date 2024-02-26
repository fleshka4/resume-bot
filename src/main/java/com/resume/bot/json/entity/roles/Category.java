package com.resume.bot.json.entity.roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

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
