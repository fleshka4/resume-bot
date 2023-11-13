package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrimaryEducation {
    @NonNull String name;
    String nameId;
    String organization;
    String organizationId;
    String result;
    String resultId;
    long year;
}
