package com.resume.bot.json.entity.metro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Metro {
    @NonNull
    private String id;

    @NonNull
    private List<Line> lines;

    @NonNull
    private String name;

    @NonNull
    private String url;

    @JsonCreator
    public static Metro createMetro(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("lines") @NonNull List<Line> lines,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("url") @NonNull String url) {
        return new Metro(id, lines, name, url);
    }
}
