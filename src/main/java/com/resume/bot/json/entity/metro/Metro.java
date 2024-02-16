package com.resume.bot.json.entity.metro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
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
