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
    private String name;

    private String url;

    @NonNull
    private List<Line> lines;

    @JsonCreator
    public static Metro createMetro(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("url") String url,
            @JsonProperty("lines") @NonNull List<Line> lines) {
        return new Metro(id, name, url, lines);
    }
}
