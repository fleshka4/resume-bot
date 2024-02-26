package com.resume.bot.json.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Type {
    @NonNull
    private String id;

    @NonNull
    private String name;
}
