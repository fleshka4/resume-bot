package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TotalExperience {
    private Long months;

    @JsonCreator
    public static TotalExperience createTotalExperience(
            @JsonProperty("months") Long months) {
        return new TotalExperience(months);
    }
}
