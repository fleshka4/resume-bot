package com.resume.bot.json.entity.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Salary {
    private Long amount;

    private String currency;
}
