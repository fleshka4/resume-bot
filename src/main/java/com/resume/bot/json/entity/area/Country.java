package com.resume.bot.json.entity.area;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Country {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String url;

    @JsonCreator
    public static Country createCountry(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("url") String url) {
        return new Country(id, name, url);
    }
}
