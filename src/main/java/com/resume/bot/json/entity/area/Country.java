package com.resume.bot.json.entity.area;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Country {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String url;
}
