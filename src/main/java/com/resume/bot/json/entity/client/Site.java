package com.resume.bot.json.entity.client;

import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Site {
    @NonNull
    private Type type;

    private String url;
}
