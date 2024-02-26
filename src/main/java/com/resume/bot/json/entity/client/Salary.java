package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Salary {
    private Long amount;

    private String currency;

    @JsonCreator
    public static Salary createSalary(
            @JsonProperty("amount") Long amount,
            @JsonProperty("currency") String currency) {
        return new Salary(amount, currency);
    }
}
