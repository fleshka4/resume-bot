package com.resume.bot.json.entity.client.education;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Course {
    @NonNull
    private String name;

    @NonNull
    private String organization;

    private String result;

    private int year;
}
