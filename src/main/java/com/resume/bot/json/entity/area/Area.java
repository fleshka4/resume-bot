package com.resume.bot.json.entity.area;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
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
