package com.resume.bot.json.entity.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Certificate {
    @NonNull
    @JsonProperty("achieved_at")
    private String achievedAt;

    private String owner;

    @NonNull
    private String title;

    @NonNull
    private String type;

    private String url;
}
