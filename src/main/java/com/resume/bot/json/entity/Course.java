package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
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
