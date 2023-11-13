package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Portfolio {
    String description;
    @NonNull String medium;
    @NonNull String small;
    @NonNull String id;
}
