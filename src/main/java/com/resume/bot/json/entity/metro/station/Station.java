package com.resume.bot.json.entity.metro.station;

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
}
