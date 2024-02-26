
package com.resume.bot.json.entity.metro;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.resume.bot.json.entity.metro.station.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

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

    @NonNull
    private List<Station> stations;
}
