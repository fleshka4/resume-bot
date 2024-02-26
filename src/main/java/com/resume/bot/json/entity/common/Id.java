package com.resume.bot.json.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Id {
    @NonNull
    private String id;
}
