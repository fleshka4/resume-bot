package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
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
