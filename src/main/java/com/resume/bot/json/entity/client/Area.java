package com.resume.bot.json.entity.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static com.resume.util.HHUriConstants.GET_AREAS_URI;

@NoArgsConstructor
@Data
public class Area {
    @NonNull
    private String id;

    @NonNull
    private String name;

    private final String url = GET_AREAS_URI + "/" + id;

    public Area(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
