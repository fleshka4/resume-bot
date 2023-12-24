package com.resume.bot.json.entity.client.education;

import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Education {
    private List<Course> additional;

    private List<Course> attestation;

    private List<ElementaryEducation> elementary;

    private Type level;

    private List<PrimaryEducation> primary;
}