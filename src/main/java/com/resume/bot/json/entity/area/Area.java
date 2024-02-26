package com.resume.bot.json.entity.area;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Data
public class Area {
    @NonNull
    private String id;

    @JsonProperty("parent_id")
    private String parentId;

    @NonNull
    private String name;

    private List<Area> areas;
}
