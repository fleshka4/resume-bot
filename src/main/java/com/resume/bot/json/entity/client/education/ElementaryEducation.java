package com.resume.bot.json.entity.client.education;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ElementaryEducation {
    @NonNull
    private String name;

    private long year;
}
