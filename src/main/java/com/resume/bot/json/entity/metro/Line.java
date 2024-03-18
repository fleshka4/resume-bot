
package com.resume.bot.json.entity.metro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resume.bot.json.entity.metro.station.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Line {
    @NonNull
    private String id;

    @JsonProperty("hex_color")
    @NonNull
    private String hexColor;

    @NonNull
    private String name;

    @NonNull
    private List<Station> stations;

    @JsonCreator
    public static Line createLine(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("hex_color") @NonNull String hexColor,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("stations") @NonNull List<Station> stations) {
        return new Line(id, hexColor, name, stations);
    }
}
