package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Portfolio {
    private String description;

    @NonNull
    private String medium;

    @NonNull
    private String small;

    @NonNull
    private String id;
}
