package com.resume.bot.json.entity.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static com.resume.util.HHUriConstants.GET_AREAS_URI;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Area {
    @NonNull
    private String id;

    @NonNull
    private String name;

    private final String url = GET_AREAS_URI + "/" + id;
}
