package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrimaryEducation {
    @NonNull
    private String name;

    @JsonProperty("name_id")
    private String nameId;

    private String organization;

    @JsonProperty("organization_id")
    private String organizationId;

    private String result;

    @JsonProperty("result_id")
    private String resultId;

    private long year;
}
