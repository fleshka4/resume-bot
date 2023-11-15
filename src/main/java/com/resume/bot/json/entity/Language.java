package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Language {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private Type level;
}
