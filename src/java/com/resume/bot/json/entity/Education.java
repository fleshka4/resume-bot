package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Education {
    List<Course> additional;
    List<Course> attestation;
    List<ElementaryEducation> elementary;
    Type level;
    List<PrimaryEducation> primary;
}
