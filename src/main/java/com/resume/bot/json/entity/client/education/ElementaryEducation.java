package com.resume.bot.json.entity.client.education;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class ElementaryEducation {
    @NonNull
    private String name;

    private long year;
}
