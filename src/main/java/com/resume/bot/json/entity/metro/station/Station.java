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
    private String name;

    @NonNull
    private Double lat;

    @NonNull
    private Double lng;

    @NonNull
    private Long order;

    @NonNull
    private Line line;

    @JsonCreator
    public static Station createStation(
            @JsonProperty("id") @NonNull String id,
            @JsonProperty("name") @NonNull String name,
            @JsonProperty("lat") @NonNull Double lat,
            @JsonProperty("lng") @NonNull Double lng,
            @JsonProperty("order") @NonNull Long order,
            @JsonProperty("line") @NonNull Line line) {
        return new Station(id, name, lat, lng, order, line);
    }
}
