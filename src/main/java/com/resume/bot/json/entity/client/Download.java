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
public class Download {
    @NonNull
    private Pdf pdf;

    @JsonCreator
    public static Download createDownload(@JsonProperty("pdf") @NonNull Pdf pdf) {
        return new Download(pdf);
    }
}
