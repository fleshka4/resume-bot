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
public class Station {
    @NonNull
    private String id;

    @NonNull
    private Double lat;

    @NonNull
    private Line line;

    @NonNull
    private Double lng;

    @NonNull
    private String name;

    @NonNull
    private Long order;

    @JsonCreator
    public static Station createStation(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("lat") @NonNull Double lat,
            @JsonProperty("line") @NonNull Line line,
            @JsonProperty("lng") @NonNull Double lng,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("order") @NonNull Long order) {
        return new Station(id, lat, line, lng, name, order);
    }
}
