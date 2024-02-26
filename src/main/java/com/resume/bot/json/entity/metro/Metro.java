package com.resume.bot.json.entity.metro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Data
public class Metro {
    @NonNull
    private String id;

    @NonNull
    private List<Line> lines;

    @NonNull
    private String name;

    @NonNull
    private String url;
}
