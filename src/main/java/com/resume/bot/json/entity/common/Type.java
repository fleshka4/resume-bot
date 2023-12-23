package com.resume.bot.json.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Type {
    @NonNull
    private String id;

    @NonNull
    private String name;
}
