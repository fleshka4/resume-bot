package com.resume.bot.json.entity.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Recommendation {
    @NonNull
    private String contact;

    @NonNull
    private String name;

    @NonNull
    private String organization;

    @NonNull
    private String position;
}
