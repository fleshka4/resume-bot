package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Locale {
    @NonNull
    private Boolean current;

    @NonNull
    private String id;

    @NonNull
    private String name;
}
