package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static com.resume.util.HHUriConstants.GET_AREAS_URI;

@NoArgsConstructor
@Data
public class Area {
//    @NonNull
    private String id;

//    @NonNull
    private String name;

    private String url = GET_AREAS_URI + "/" + id;

    public Area(//@NonNull
                String id,
                //@NonNull
                String name) {
        this.id = id;
        this.name = name;
        url = GET_AREAS_URI + "/" + id;
    }

    public void setId(String id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }
        this.id = id;
        url = GET_AREAS_URI + "/" + id;
    }

    @JsonCreator
    public static Area createArea(
            @JsonProperty("id")
            //@NonNull
            String id,
            @JsonProperty("name")
            //@NonNull
            String name) {
        return new Area(id, name);
    }
}
