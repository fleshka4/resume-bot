package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Recommendation {
    @NonNull String contact;
    @NonNull String name;
    @NonNull String organization;
    @NonNull String position;
}
