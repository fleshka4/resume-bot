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
public class Photo {
    @NonNull
    private String medium;

    @NonNull
    private String small;

    @NonNull
    private String id;

    @JsonCreator
    public static Photo createPhoto(
            @JsonProperty("medium") @NonNull String medium,
            @JsonProperty("small") @NonNull String small,
            @JsonProperty("id") @NonNull String id) {
        return new Photo(medium, small, id);
    }
}
