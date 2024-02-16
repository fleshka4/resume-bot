package com.resume.bot.json.entity.metro.station;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

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
}
