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
public class Pdf {
    @NonNull
    private String url;

    @JsonCreator
    public static Pdf createPdf(@JsonProperty("url") @NonNull String url) {
        return new Pdf(url);
    }
}
