package com.resume.bot.json.entity.metro.station;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Line {
    @JsonProperty("hex_color")
    @NonNull
    private String hexColor;

    @NonNull
    private String id;

    @NonNull
    private String name;

    @JsonCreator
    public static Line createLine(
            @JsonProperty("hex_color") @NonNull String hexColor,
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name) {
        return new Line(hexColor, id, name);
    }
}
