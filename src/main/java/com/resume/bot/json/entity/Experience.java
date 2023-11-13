package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Experience {
    Area area;
    String company;
    String companyId;
    String companyUrl;
    String description;
    Employer employer;
    String end;
    @NonNull List<Type> industries;
    Type industry;
    @NonNull String position;
    @NonNull String start;
}
