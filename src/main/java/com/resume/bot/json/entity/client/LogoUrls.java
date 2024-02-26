package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LogoUrls {
//    @NonNull
    @JsonProperty("90")
    private String ninety;

//    @NonNull
    @JsonProperty("240")
    private String twoForty;

//    @NonNull
    private String original;
}
