package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public static Phone createPhone(
            @JsonProperty("city") String city,
            @JsonProperty("country") String country,
            @JsonProperty("formatted") String formatted,
            @JsonProperty("number") String number) {
        return new Phone(city, country, formatted, number);
    }
}
