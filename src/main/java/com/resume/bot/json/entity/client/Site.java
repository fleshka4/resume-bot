package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resume.bot.json.entity.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Site {
    @NonNull
    private Type type;

    private String url;

    @JsonCreator
    public static Site createSite(
            @JsonProperty("type") @NonNull Type type,
            @JsonProperty("url") String url) {
        return new Site(type, url);
    }
}
