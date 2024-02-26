package com.resume.bot.json.entity.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Phone {
    @NonNull
    private String city;

    @NonNull
    private String country;

    @NonNull
    private String formatted;

    @NonNull
    private String number;
}
