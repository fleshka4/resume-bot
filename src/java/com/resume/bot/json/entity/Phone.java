package com.resume.bot.json.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Phone {
    @NonNull String city;
    @NonNull String country;
    @NonNull String formatted;
    @NonNull String number;
}
